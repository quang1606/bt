/* ---- Xử lý tạo review ---- */
const reviewForm = document.getElementById("review-form");
const reviewContentEl = document.getElementById("review-content");

// Sử dụng biến global cho rating đã chọn (được cập nhật từ phần chọn rating)
let currentRating = 0;

// Xử lý chọn rating (code hiện tại đã có)
const stars = document.querySelectorAll(".star");
const ratingValue = document.getElementById("rating-value");

stars.forEach((star) => {
    star.addEventListener("mouseover", () => {
        resetStars();
        const rating = parseInt(star.getAttribute("data-rating"));
        highlightStars(rating);
    });

    star.addEventListener("mouseout", () => {
        resetStars();
        highlightStars(currentRating);
    });

    star.addEventListener("click", () => {
        currentRating = parseInt(star.getAttribute("data-rating"));
        ratingValue.textContent = `Bạn đã đánh giá ${currentRating} sao.`;
        highlightStars(currentRating);
    });
});
function resetStars() {
    stars.forEach((star) => {
        star.classList.remove("active");
    });
}

function highlightStars(rating) {
    stars.forEach((star) => {
        const starRating = parseInt(star.getAttribute("data-rating"));
        if (starRating <= rating) {
            star.classList.add("active");
        }
    });
}


// Hàm format ngày theo định dạng dd/MM/yyyy
const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    let day = date.getDate();
    let month = date.getMonth() + 1;
    let year = date.getFullYear();
    day = day < 10 ? '0' + day : day;
    month = month < 10 ? '0' + month : month;
    return `${day}/${month}/${year}`;
};

// Hàm render danh sách reviews sử dụng template string
const renderReviews = (reviews) => {
    const html = reviews.map(review => `
        <div class="review-item">
            <div class="review-author d-flex justify-content-between">
                <div class="d-flex">
                    <div class="review-author-avatar">
                        <img src="${review.user.avatar || 'https://homepage.momocdn.net/cinema/momo-cdn-api-220615142913-637909001532744942.jpg'}" alt="">
                    </div>
                    <div class="ms-3">
                        <p class="fw-semibold">${review.user.displayName}</p>
                        <p>⭐️ ${review.rating}/10</p>
                    </div>
                </div>
                <div>
                    <p>${formatDate(review.createdAt)}</p>
                </div>
            </div>
            <div class="review-content mt-3">
                <p>${review.content}</p>
            </div>
            <div class="review-actions mt-2">
                <button class="btn btn-sm btn-primary" onclick="handleEditReviewClick(event, ${review.id}, '${review.content}', ${review.rating})" >Sửa</button>
                <button class="btn btn-sm btn-danger" onclick="deleteReview(${review.id})">Xóa</button>
            </div>
        </div>
    `).join('');
    document.querySelector('.review-list').innerHTML = html;
};

// Hàm render phân trang sử dụng template string
const renderPagination = (totalPages, currentPage) => {
    let paginationHTML = `
        <ul class="pagination justify-content-center">
            ${currentPage <= 1
        ? `<li class="page-item disabled"><button class="page-link" disabled>Previous</button></li>`
        : `<li class="page-item"><button class="page-link" onclick="getReviews(${currentPage - 1})">Previous</button></li>`
    }
    `;

    for (let i = 1; i <= totalPages; i++) {
        paginationHTML += i === currentPage
            ? `<li class="page-item active"><button class="page-link" onclick="getReviews(${i})">${i}</button></li>`
            : `<li class="page-item"><button class="page-link" onclick="getReviews(${i})">${i}</button></li>`;
    }

    paginationHTML += `
            ${currentPage >= totalPages
        ? `<li class="page-item disabled"><button class="page-link" disabled>Next</button></li>`
        : `<li class="page-item"><button class="page-link" onclick="getReviews(${currentPage + 1})">Next</button></li>`
    }
        </ul>
    `;
    document.querySelector('nav.mt-4').innerHTML = paginationHTML;
};


// Hàm lấy reviews (sử dụng async/await, arrow function và try/catch)
const getReviews = async (page) => {
    try {
        const movieId = movie.id;
        const response = await axios.get('/api/reviews', {
            params: {
                movieId: movieId, // Sửa thành movieId = 1
                page: page,
                pageSize: 5
            }
        });

        // Giả sử API trả về đối tượng có cấu trúc:
        // { content: [...reviews], totalPages: number, number: currentPage (0-indexed) }
        const reviewPage = response.data;
        renderReviews(reviewPage.content);
        renderPagination(reviewPage.totalPages, reviewPage.number + 1);
    } catch (error) {
        console.log(error);
    }
};

// Hàm xóa review sử dụng async/await và window.confirm
const deleteReview = async (id) => {
    if (!window.confirm("Bạn có chắc chắn muốn xóa review này không?")) {
        return;
    }
    try {
        await axios.delete(`/api/reviews/${id}`);
        // Nếu cần cập nhật lại danh sách review cho trang 1, có thể gọi:
        getReviews(1);
    } catch (error) {
        console.log(error);
    }
};




// Xử lý submit form tạo review
async function handleCreateReview(event) {
    event.preventDefault();

    // Validate content
    const content = reviewContentEl.value.trim();
    if (!content) {
        alert("Vui lòng nhập nội dung");
        return;
    }

    // Tạo payload cho API
    const payload = {
        content: content,
        rating: currentRating,
        movieId: movie.id,
    };

    try {
        await axios.post('/api/reviews', payload);
        alert("Tạo review thành công");

        // Đóng modal sử dụng Bootstrap 5
        const modalEl = document.getElementById('modal-review');
        const modalInstance = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
        modalInstance.hide();

        // Reset form và UI rating
        reviewForm.reset();
        currentRating = 0;
        resetStars();
        ratingValue.textContent = "";

        // Nếu muốn, có thể cập nhật lại danh sách review ngay sau khi tạo
        getReviews(1);
    } catch (error) {
        console.log(error);
    }
}
async function handleUpdateReview(event, reviewId) {
    event.preventDefault();

    // Kiểm tra rating có được chọn không
    if (currentRating === 0) {
        alert("Vui lòng chọn rating");
        return;
    }

    // Kiểm tra nội dung review có bị trống không
    const content = reviewContentEl.value.trim();
    if (!content) {
        alert("Vui lòng nhập nội dung");
        return;
    }

    // Chuẩn bị dữ liệu cập nhật
    const payload = {
        content: content,
        rating: currentRating
    };

    try {
        // Gửi PUT request đến API để cập nhật review
        await axios.put(`/api/reviews/${reviewId}`, payload);
        alert("Cập nhật review thành công");

        // Đóng modal chỉnh sửa
        const modalEl = document.getElementById('modal-review');
        const modalInstance = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
        modalInstance.hide();

        // Reset form và UI rating
        reviewForm.reset();
        currentRating = 0;
        resetStars();
        ratingValue.textContent = "";

        // Cập nhật danh sách review sau khi chỉnh sửa
        getReviews(1);
    } catch (error) {
        console.log(error);
        alert("Cập nhật review thất bại!");
    }
}

// Hàm sửa review
async function handleEditReviewClick(event, reviewId, reviewContent, reviewRating) {
    // Điền dữ liệu vào form
    reviewContentEl.value = reviewContent;
    currentRating = parseInt(reviewRating);
    highlightStars(currentRating);
    ratingValue.textContent = `Bạn đã đánh giá ${currentRating} sao.`;

    // Hiển thị modal
    const modalEl = document.getElementById('modal-review');
    const modalInstance = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
    modalInstance.show();

    // Cập nhật trạng thái và id review
    isUpdating = true;
    currentReviewId = reviewId;

    // Loại bỏ event listener tạo review và thêm event listener cập nhật review
    reviewForm.removeEventListener("submit", handleFormSubmit);
    reviewForm.addEventListener("submit", handleFormSubmit);
}

// Hàm xử lý submit form (tạo hoặc cập nhật)
async function handleFormSubmit(event) {
    if (isUpdating) {
        await handleUpdateReview(event, currentReviewId);
    } else {
        await handleCreateReview(event);
    }
    // Reset trạng thái
    isUpdating = false;
    currentReviewId = null;
    //Loại bỏ chính event listener
    reviewForm.removeEventListener("submit", handleFormSubmit);
    //Thêm lại event listener tạo review
    reviewForm.addEventListener("submit", handleFormSubmit);
}
// Thêm event listener tạo review (sử dụng tên hàm)
reviewForm.addEventListener("submit", handleFormSubmit);

// Khởi chạy load trang đầu tiên
getReviews(1);