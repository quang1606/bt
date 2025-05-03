// --- Constants ---
const API_ENDPOINT = '/api/admin/movies';
const PAGE_SIZE = 10;
const movieTableBody = document.querySelector('[data-movies-container]');
const paginationContainer = document.getElementById('pagination');

// --- Helper Functions ---

const formatDate = (dateStr) => {
    if (!dateStr) return 'N/A';
    try {
        // Sử dụng định dạng phù hợp nếu API trả về kiểu khác ISO string
        return new Date(dateStr).toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' });
    } catch (e) {
        console.error("Error formatting date:", dateStr, e);
        return 'Invalid Date';
    }
}

const formatStatus = (status) => {
    return status ? '<span class="badge bg-success">Công khai</span>' : '<span class="badge bg-secondary">Ẩn</span>';
}

// Helper để lấy số cột từ thead (dùng cho colspan)
const getTableColumnCount = (tableBody) => {
    // Tìm table cha -> tìm thead -> tìm tr đầu tiên -> đếm số th/td con
    return tableBody?.closest('table')?.querySelector('thead tr')?.childElementCount || 5; // Mặc định là 5 nếu không tìm thấy
}

// --- Rendering Functions ---

const renderMovies = (movies) => {
    if (!movieTableBody) {
        console.error("Element with [data-movies-container] not found for rendering movies.");
        return;
    }

    const columnCount = getTableColumnCount(movieTableBody);

    if (!movies || movies.length === 0) {
        movieTableBody.innerHTML = `<tr><td colspan="${columnCount}" class="text-center">Không có dữ liệu phim.</td></tr>`;
        return;
    }

    const html = movies.map(movie => {
        // !!! QUAN TRỌNG: Xác nhận tên trường trả về từ API là 'title' hay 'name'
        const safeTitle = movie.name ? movie.name.replace(/</g, "<").replace(/>/g, ">") : 'Không có tiêu đề';
        // const safeTitle = movie.name ? movie.name.replace(/</g, "<").replace(/>/g, ">") : 'Không có tiêu đề'; // Nếu API trả về 'name'

        const safeType = movie.type ? movie.type.replace(/</g, "<").replace(/>/g, ">") : 'N/A';
        const releaseYearDisplay = formatDate(movie.releaseYear)  || 'N/A';
        const formattedDate = formatDate(movie.updateAt);
        const statusBadge = formatStatus(movie.status);

        // Điều chỉnh URL chi tiết nếu cần
        const detailUrl = movie.id ? `/admin/movies/${movie.id}` : '#'; // Ví dụ URL admin

        return `
            <tr>
                <td>
                    <a href="${detailUrl}">${safeTitle}</a>
                </td>
                <td>${safeType}</td>
                <td>${releaseYearDisplay}</td>
                <td>${statusBadge}</td>
                <td>${formattedDate}</td>
                <!-- Actions Buttons (Uncomment if needed) -->
                <!--
                <td>
                    <button class="btn btn-sm btn-info" onclick="viewMovie(${movie.id})">Xem</button>
                    <button class="btn btn-sm btn-warning" onclick="editMovie(${movie.id})">Sửa</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteMovie(${movie.id})">Xóa</button>
                </td>
                -->
            </tr>
        `;
    }).join('');
    movieTableBody.innerHTML = html;
};

const renderPagination = (totalPages, currentPage) => {
    // const paginationContainer = document.getElementById('pagination'); // Đã khai báo ở trên
    if (!paginationContainer) {
        console.error("Pagination container with ID 'pagination' not found!");
        return;
    }
    if (totalPages <= 1) {
        paginationContainer.innerHTML = '';
        return;
    }

    let paginationHTML = '<ul class="pagination mb-0">';
    const maxVisiblePages = 5;
    const halfVisible = Math.floor(maxVisiblePages / 2);

    // Previous Button
    paginationHTML += `
        <li class="paginate_button page-item previous ${currentPage <= 1 ? 'disabled' : ''}">
            <a href="#" aria-controls="example2" data-dt-idx="${currentPage - 2}" tabindex="0"
               class="page-link" ${currentPage <= 1 ? 'aria-disabled="true" style="pointer-events: none;"' : `onclick="event.preventDefault(); getMovies(${currentPage - 1})"`}>Previous</a>
        </li>`;

    // Page 1
    paginationHTML += `
        <li class="paginate_button page-item ${1 === currentPage ? 'active' : ''}">
            <a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0"
               class="page-link" onclick="event.preventDefault(); if(1 !== ${currentPage}) getMovies(1);">1</a>
        </li>`;

    // Ellipsis and Middle Pages
    let startPage = Math.max(2, currentPage - halfVisible);
    let endPage = Math.min(totalPages - 1, currentPage + halfVisible);

    if (currentPage - halfVisible <= 2) endPage = Math.min(totalPages - 1, maxVisiblePages);
    if (currentPage + halfVisible >= totalPages - 1) startPage = Math.max(2, totalPages - maxVisiblePages + 1);

    if (startPage > 2) {
        paginationHTML += `<li class="paginate_button page-item disabled"><a href="#" class="page-link" aria-disabled="true">...</a></li>`;
    }

    for (let i = startPage; i <= endPage; i++) {
        paginationHTML += `
            <li class="paginate_button page-item ${i === currentPage ? 'active' : ''}">
                <a href="#" aria-controls="example2" data-dt-idx="${i - 1}" tabindex="0"
                   class="page-link" onclick="event.preventDefault(); if(${i} !== ${currentPage}) getMovies(${i});">${i}</a>
            </li>`;
    }

    if (endPage < totalPages - 1) {
        paginationHTML += `<li class="paginate_button page-item disabled"><a href="#" class="page-link" aria-disabled="true">...</a></li>`;
    }

    // Last Page (if totalPages > 1)
    if (totalPages > 1) {
        paginationHTML += `
            <li class="paginate_button page-item ${totalPages === currentPage ? 'active' : ''}">
                <a href="#" aria-controls="example2" data-dt-idx="${totalPages - 1}" tabindex="0"
                   class="page-link" onclick="event.preventDefault(); if(${totalPages} !== ${currentPage}) getMovies(${totalPages});">${totalPages}</a>
            </li>`;
    }

    // Next Button
    paginationHTML += `
        <li class="paginate_button page-item next ${currentPage >= totalPages ? 'disabled' : ''}">
            <a href="#" aria-controls="example2" data-dt-idx="${currentPage}" tabindex="0"
               class="page-link" ${currentPage >= totalPages ? 'aria-disabled="true" style="pointer-events: none;"' : `onclick="event.preventDefault(); getMovies(${currentPage + 1})"`}>Next</a>
        </li>`;

    paginationHTML += '</ul>';
    paginationContainer.innerHTML = paginationHTML;
};

// --- API Call Function ---
const getMovies = async (page = 1) => {
    const columnCount = getTableColumnCount(movieTableBody); // Lấy số cột trước khi xóa
    if (movieTableBody) {
        movieTableBody.innerHTML = `<tr><td colspan="${columnCount}" class="text-center">Đang tải...</td></tr>`;
    }
    if (paginationContainer) {
        paginationContainer.innerHTML = '';
    }

    try {
        // *** Đảm bảo bạn đã nhúng thư viện axios hoặc thay bằng fetch ***
        const response = await axios.get(API_ENDPOINT, {
            params: { page: page, pageSize: PAGE_SIZE }
        });

        const moviePage = response.data;

console.log(moviePage)
        if (moviePage && typeof moviePage.content !== 'undefined' && typeof moviePage.totalPages !== 'undefined' && typeof moviePage.number !== 'undefined') {
            const currentPageForDisplay = moviePage.number + 1;
            renderMovies(moviePage.content); // Render bảng trước
            renderPagination(moviePage.totalPages, currentPageForDisplay); // Sau đó render phân trang
        } else {
            console.error("API response format is incorrect:", moviePage);
            if (movieTableBody) {
                movieTableBody.innerHTML = `<tr><td colspan="${columnCount}" class="text-center">Lỗi định dạng dữ liệu từ API.</td></tr>`;
            }
        }

    } catch (error) {
        console.error("Error fetching movie list:", error);
        if (movieTableBody) {
            let errorMsg = "Đã xảy ra lỗi khi tải dữ liệu. Vui lòng thử lại.";
            if (error.response) {
                // Lỗi từ server (vd: 404, 500)
                errorMsg += ` (Status: ${error.response.status})`;
            } else if (error.request) {
                // Request đã gửi nhưng không nhận được response (lỗi mạng?)
                errorMsg += " (Network Error)";
            }
            movieTableBody.innerHTML = `<tr><td colspan="${columnCount}" class="text-center">${errorMsg}</td></tr>`;
        }
    } finally {
        // Ví dụ: tắt spinner loading nếu có
    }
};

// --- Action Functions (Placeholders - Uncomment buttons in renderMovies to use) ---
function viewMovie(id) {
    console.log("Xem phim ID:", id);
    // window.location.href = `/admin/movies/${id}`; // Example redirect
}

function editMovie(id) {
    console.log("Sửa phim ID:", id);
    // window.location.href = `/admin/movies/edit/${id}`; // Example redirect
}

async function deleteMovie(id) { // Nên dùng async nếu gọi API xóa
    console.log("Xóa phim ID:", id);
    if (confirm(`Bạn có chắc chắn muốn xóa phim ID ${id} không?`)) {
        try {
            // Uncomment để gọi API xóa
            // await axios.delete(`${API_ENDPOINT}/${id}`);
            // alert('Xóa thành công!');
            // getMovies(); // Tải lại trang hiện tại hoặc trang đầu
            console.log("Simulating delete success for ID:", id); // Bỏ dòng này khi dùng API thật
            alert('Giả lập xóa thành công!'); // Bỏ dòng này khi dùng API thật

        } catch (error) {
            console.error("Lỗi khi xóa phim:", error);
            alert('Xóa thất bại. Vui lòng kiểm tra console.');
        }
    }
}

// --- Initialization ---
document.addEventListener('DOMContentLoaded', () => {
    // Chỉ gọi getMovies một lần khi DOM đã sẵn sàng
    if (movieTableBody && paginationContainer) { // Kiểm tra các thành phần chính tồn tại
        getMovies(1); // Tải trang đầu tiên
    } else {
        console.error("Cannot initialize movie list: Missing table body or pagination container.");
    }
});