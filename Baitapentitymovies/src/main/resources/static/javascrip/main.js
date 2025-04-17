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



const favoriteButton  = document.getElementById('favorite-btn');
const favoriteAlertt = document.getElementById('favorite-alert');

// Giả sử bạn có movieId được nhúng từ server
const movieId = movie.id;
let isFavoritee = false;

if (typeof currentUser !== 'undefined' && currentUser !== null) {
    const userId = currentUser.id;
    // Gọi API kiểm tra yêu thích
    fetch(`/api/favourite/${userId}/${movieId}`)
        .then(response => response.json())
        .then(data => {
            isFavoritee = data;
            updateFavoriteUI();
        })
        .catch(error => {
            console.error('Lỗi khi kiểm tra trạng thái yêu thích:', error);
        });
} else {
    console.warn('Người dùng chưa đăng nhập.');
    // Có thể disable nút hoặc hiển thị yêu cầu đăng nhập
    updateFavoriteUI(); // Gọi để đảm bảo UI đúng ngay cả khi chưa login
}

// Hàm cập nhật giao diện theo trạng thái yêu thích
function updateFavoriteUI() {
    const favoriteButton = document.getElementById('favorite-btn');

    if (!favoriteButton) return;

    if (typeof currentUser === 'undefined' || currentUser === null) {
        favoriteButton.textContent = 'Đăng nhập để thêm yêu thích';
        favoriteButton.classList.remove('btn-primary');
        favoriteButton.classList.add('btn-outline-secondary');
        favoriteButton.disabled = true;
    } else {
        favoriteButton.textContent = isFavoritee ? 'Đã thêm vào danh sách yêu thích' : 'Thêm vào danh sách yêu thích';
        favoriteButton.classList.toggle('btn-primary', isFavoritee);
        favoriteButton.classList.toggle('btn-outline-primary', !isFavoritee);
    }
}




favoriteButton.addEventListener('click', () => {
    isFavoritee = !isFavoritee;

    // Cập nhật giao diện
    favoriteButton.textContent = isFavoritee ? 'Đã thêm vào danh sách yêu thích' : 'Thêm vào danh sách yêu thích';
    favoriteButton.classList.toggle('btn-primary', isFavoritee);
    favoriteButton.classList.toggle('btn-outline-primary', !isFavoritee);

    favoriteAlertt.textContent = isFavoritee
        ? 'Phim đã được thêm vào danh sách yêu thích!'
        : 'Phim đã bị xóa khỏi danh sách yêu thích!';
    favoriteAlertt.classList.remove('alert-danger', 'alert-success');
    favoriteAlertt.classList.add(isFavoritee ? 'alert-success' : 'alert-danger');
    favoriteAlertt.style.display = 'block';

    setTimeout(() => {
        favoriteAlertt.style.display = 'none';
    }, 3000);

    // Gọi API tương ứng
    if (isFavoritee) {
        fetch(`/api/favourite/${movieId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ movieId: movieId })

        }).then(response => {
            if (!response.ok) throw new Error('Lỗi khi thêm vào yêu thích');
        }).catch(err => {
            console.error(err);
            alert('Có lỗi xảy ra khi thêm vào danh sách yêu thích!');
        });
    } else {
        fetch(`/api/favourite/${movieId}`, {
            method: 'DELETE'
        }).then(response => {
            if (!response.ok) throw new Error('Lỗi khi xóa khỏi yêu thích');
        }).catch(err => {
            console.error(err);
            alert('Có lỗi xảy ra khi xóa khỏi danh sách yêu thích!');
        });
    }
});



// Hàm format ngày theo định dạng dd/MM/yyyy
const formatDate = (dateStr) =>
   new Date(dateStr).toLocaleDateString();

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
            <div  class="review-actions mt-2">
                <button class="btn btn-sm btn-primary edit-btn" onclick="handleEditReviewClick(event, ${review.id}, '${review.content}', ${review.rating})" >Sửa</button>
                <button class="btn btn-sm btn-danger delete-btn" onclick="deleteReview(${review.id})">Xóa</button>
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
                movieId: movieId,
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
