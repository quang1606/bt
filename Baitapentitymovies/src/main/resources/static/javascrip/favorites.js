// Hàm hiển thị danh sách phim yêu thích
const renderFavoriteMovies = (favoriteMovies) => {
    const html = favoriteMovies.map(movie => `  
        <div class="col-2">  
            <div class="movie-item">  
                <div class="movie-poster rounded overflow-hidden position-relative">  
                    <img src="${movie.thumbnail || 'https://placehold.co/600x400?text=No+Image'}" alt="${movie.title}">  
                </div>  
                <div class="movie-info">  
                    <p class="mt-3 mb-0">${movie.name}</p>  
                    <button class="btn btn-sm btn-danger" onclick="deleteFavoriteMovie(${movie.id})">Xóa</button>  
                </div>  
            </div>  
        </div>  
    `).join('');

    document.querySelector('#favorite-movie-list').innerHTML = html;
};

// Hàm lấy phim yêu thích (sử dụng async/await)
let currentPage = 1; // Biến để lưu trang hiện tại
const getFavoriteMovies = async (page = 1) => {
    try {
        const response = await axios.get('/api/favourite', {
            params: {
                page: page,
                pageSize: 12
            }
        });
        const favoriteMovies = response.data.content || []; // Giả sử API trả về danh sách phim yêu thích
        renderFavoriteMovies(favoriteMovies);
        renderPagination(response.data.totalPages, response.data.number + 1); // Sử dụng response từ API
        currentPage = page; // Cập nhật trang hiện tại
    } catch (error) {
        console.log(error);
    }
};

// Hàm xóa từng phim yêu thích
const deleteFavoriteMovie = async (id) => {
    if (!window.confirm("Bạn có chắc chắn muốn xóa phim này không?")) {
        return;
    }
    try {
        await axios.delete(`/api/favourite/${id}`); // API giả định
        getFavoriteMovies(currentPage); // Cập nhật danh sách sau khi xóa
    } catch (error) {
        console.log(error);
    }
};

// Hàm xóa tất cả phim yêu thích
const deleteAllFavoriteMovies = async () => {
    if (!window.confirm("Bạn có chắc chắn muốn xóa tất cả phim yêu thích không?")) {
        return;
    }
    try {
        await axios.delete('/api/favourite/all'); // API giả định
        getFavoriteMovies(); // Cập nhật danh sách sau khi xóa
    } catch (error) {
        console.log(error);
    }
};

// Hàm hiển thị phân trang
const renderPagination = (totalPages, currentPage) => {
    let paginationHTML = `  
        <ul class="pagination justify-content-center">  
            ${currentPage <= 1
        ? `<li class="page-item disabled"><button class="page-link" disabled>Previous</button></li>`
        : `<li class="page-item"><button class="page-link" onclick="getFavoriteMovies(${currentPage - 1})">Previous</button></li>`
    }  
    `;

    for (let i = 1; i <= totalPages; i++) {
        paginationHTML += i === currentPage
            ? `<li class="page-item active"><button class="page-link" onclick="getFavoriteMovies(${i})">${i}</button></li>`
            : `<li class="page-item"><button class="page-link" onclick="getFavoriteMovies(${i})">${i}</button></li>`;
    }

    paginationHTML += `  
            ${currentPage >= totalPages
        ? `<li class="page-item disabled"><button class="page-link" disabled>Next</button></li>`
        : `<li class="page-item"><button class="page-link" onclick="getFavoriteMovies(${currentPage + 1})">Next</button></li>`
    }  
        </ul>  
    `;
    document.querySelector('nav.mt-4').innerHTML = paginationHTML;
};




// Gọi hàm lấy phim yêu thích lúc load trang
document.addEventListener("DOMContentLoaded", () => {
    getFavoriteMovies(); // Gọi hàm với trang mặc định
    document.getElementById('delete-all-btn').onclick = deleteAllFavoriteMovies; // Liên kết nút xóa tất cả
});


