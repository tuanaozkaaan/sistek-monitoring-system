(function () {
    const panel = document.getElementById('barcode-panel');
    if (!panel) {
        return;
    }

    const fetchUrl = panel.dataset.fetchUrl;
    const pageSize = Number(panel.dataset.pageSize || 20);
    const totalPages = Number(panel.dataset.totalPages || 0);
    const totalElements = Number(panel.dataset.totalElements || 0);
    const showLineColumn = panel.dataset.showLineColumn === 'true';
    const columnCount = Number(panel.dataset.columnCount || 4);

    const tableBody = document.getElementById('barcode-table-body');
    const paginationInfo = document.getElementById('pagination-info');
    const paginationPages = document.getElementById('pagination-pages');
    const prevBtn = document.getElementById('page-prev');
    const nextBtn = document.getElementById('page-next');
    const loadingHint = document.getElementById('pagination-loading');

    let currentPage = Number(panel.dataset.currentPage || 0);
    let isLoading = false;

    function statusBadgeClass(status) {
        const normalized = (status || '').toUpperCase();
        if (normalized === 'RUN' || normalized === 'ACTIVE' || normalized === 'SENT' || normalized === 'NEW') {
            return 'badge-run';
        }
        if (normalized === 'STOP' || normalized === 'ERROR' || normalized === 'NOCOMM') {
            return 'badge-stop';
        }
        if (normalized === 'PASSIVE') {
            return 'badge-passive';
        }
        return 'badge-default';
    }

    function formatDateTime(value) {
        if (!value) {
            return '-';
        }
        const date = new Date(value);
        if (Number.isNaN(date.getTime())) {
            return '-';
        }
        const pad = (part) => String(part).padStart(2, '0');
        return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} `
            + `${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
    }

    function createStatusBadge(status) {
        const badge = document.createElement('span');
        badge.className = `badge-status ${statusBadgeClass(status)}`;
        badge.textContent = status || '-';
        return badge;
    }

    function formatNumber(value) {
        return new Intl.NumberFormat().format(value);
    }

    function updatePaginationInfo() {
        if (!paginationInfo || totalElements === 0) {
            return;
        }
        const from = currentPage * pageSize + 1;
        const to = Math.min((currentPage + 1) * pageSize, totalElements);
        paginationInfo.textContent = `Showing ${formatNumber(from)}–${formatNumber(to)} of ${formatNumber(totalElements)} barcodes`;
    }

    function getVisiblePages() {
        if (totalPages <= 1) {
            return [];
        }

        const pages = new Set([0, totalPages - 1]);
        for (let page = currentPage - 2; page <= currentPage + 2; page += 1) {
            if (page >= 0 && page < totalPages) {
                pages.add(page);
            }
        }

        const sorted = [...pages].sort((a, b) => a - b);
        const items = [];

        sorted.forEach((page, index) => {
            if (index > 0 && page - sorted[index - 1] > 1) {
                items.push({ type: 'ellipsis' });
            }
            items.push({ type: 'page', page });
        });

        return items;
    }

    function renderPaginationControls() {
        if (!paginationPages || totalPages <= 1) {
            return;
        }

        paginationPages.innerHTML = '';

        getVisiblePages().forEach((item) => {
            if (item.type === 'ellipsis') {
                const ellipsis = document.createElement('span');
                ellipsis.className = 'pagination-ellipsis';
                ellipsis.textContent = '…';
                paginationPages.appendChild(ellipsis);
                return;
            }

            const button = document.createElement('button');
            button.type = 'button';
            button.className = 'pagination-btn';
            if (item.page === currentPage) {
                button.classList.add('is-active');
            }
            button.textContent = String(item.page + 1);
            button.setAttribute('aria-label', `Page ${item.page + 1}`);
            button.addEventListener('click', () => goToPage(item.page));
            paginationPages.appendChild(button);
        });

        if (prevBtn) {
            prevBtn.disabled = currentPage === 0 || isLoading;
        }
        if (nextBtn) {
            nextBtn.disabled = currentPage >= totalPages - 1 || isLoading;
        }
    }

    function createLineLink(lineId) {
        const link = document.createElement('a');
        link.href = `/dashboard/lines/${encodeURIComponent(lineId)}`;
        link.textContent = lineId;
        link.className = 'line-link';
        return link;
    }

    function renderBarcodeRows(barcodes) {
        tableBody.innerHTML = '';

        if (!barcodes.length) {
            const row = document.createElement('tr');
            const cell = document.createElement('td');
            cell.colSpan = columnCount;
            cell.className = 'empty-state';
            cell.textContent = 'No barcodes on this page.';
            row.appendChild(cell);
            tableBody.appendChild(row);
            return;
        }

        barcodes.forEach((barcode) => {
            const row = document.createElement('tr');

            const barcodeCell = document.createElement('td');
            barcodeCell.textContent = barcode.barcode;
            row.appendChild(barcodeCell);

            if (showLineColumn) {
                const lineCell = document.createElement('td');
                lineCell.appendChild(createLineLink(barcode.lineId));
                row.appendChild(lineCell);
            }

            const statusCell = document.createElement('td');
            statusCell.appendChild(createStatusBadge(barcode.status));
            row.appendChild(statusCell);

            const createdCell = document.createElement('td');
            createdCell.textContent = formatDateTime(barcode.creDate);
            row.appendChild(createdCell);

            const lastStatusCell = document.createElement('td');
            lastStatusCell.textContent = formatDateTime(barcode.lastStatusDate);
            row.appendChild(lastStatusCell);

            tableBody.appendChild(row);
        });
    }

    function setLoadingState(loading) {
        isLoading = loading;
        if (loadingHint) {
            loadingHint.classList.toggle('d-none', !loading);
        }
        renderPaginationControls();
    }

    async function goToPage(page) {
        if (isLoading || page === currentPage || page < 0 || page >= totalPages) {
            return;
        }

        setLoadingState(true);

        try {
            const response = await fetch(
                `${fetchUrl}?page=${page}&size=${pageSize}`
            );

            if (!response.ok) {
                throw new Error(`Request failed with status ${response.status}`);
            }

            const data = await response.json();
            currentPage = data.page;
            panel.dataset.currentPage = String(currentPage);
            renderBarcodeRows(data.content || []);
            updatePaginationInfo();
            renderPaginationControls();
            panel.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
        } catch (error) {
            console.error('Failed to load barcode page', error);
        } finally {
            setLoadingState(false);
        }
    }

    if (prevBtn) {
        prevBtn.addEventListener('click', () => goToPage(currentPage - 1));
    }

    if (nextBtn) {
        nextBtn.addEventListener('click', () => goToPage(currentPage + 1));
    }

    updatePaginationInfo();
    renderPaginationControls();
})();
