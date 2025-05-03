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
    // Lấy ID người dùng đang đăng nhập (nếu có)
    // Quan trọng: Đảm bảo biến `currentUser` được định nghĩa trong scope này
    // hoặc là biến global.
    const loggedInUserId = (typeof currentUser !== 'undefined' && currentUser && currentUser.id) ? currentUser.id : null;

    const html = reviews.map(review => {
        // Kiểm tra xem người dùng hiện tại có phải là tác giả của review này không
        // Quan trọng: Đảm bảo API trả về review.user.id
        const isAuthor = loggedInUserId && review.user && review.user.id && loggedInUserId === review.user.id;

        // Tạo chuỗi HTML cho các nút hành động (chỉ khi là tác giả)
        let actionButtonsHTML = '';
        if (isAuthor) {
            // --- QUAN TRỌNG ---
            // Để tránh lỗi XSS và lỗi cú pháp khi content chứa ký tự đặc biệt (như ', ", `),
            // hãy escape nó đúng cách khi đưa vào thuộc tính onclick.
            // Sử dụng JSON.stringify là một cách an toàn và hiệu quả.
            const escapedContent = JSON.stringify(review.content);

            actionButtonsHTML = `
                <button class="btn btn-sm btn-primary edit-btn" onclick='handleEditReviewClick(event, ${review.id}, ${escapedContent}, ${review.rating})'>Sửa</button>
                <button class="btn btn-sm btn-danger delete-btn" onclick="deleteReview(${review.id})">Xóa</button>
            `;
            // Lưu ý: Đổi dấu nháy bao quanh onclick thành nháy đơn (') để không xung đột với nháy kép (") của JSON.stringify
        }

        // --- CẢNH BÁO BẢO MẬT ---
        // Việc chèn trực tiếp review.content vào HTML như dưới đây có thể gây ra lỗ hổng XSS
        // nếu nội dung chứa mã HTML/JavaScript độc hại.
        // Bạn NÊN sanitize (làm sạch) nội dung này ở phía server TRƯỚC KHI lưu vào DB
        // hoặc escape nó ở phía client TRƯỚC KHI hiển thị.
        // Ví dụ đơn giản (nhưng chưa đủ hoàn toàn): thay thế < > &
        const safeContent = review.content.replace(/</g, "<").replace(/>/g, ">").replace(/&/g, "&");
        // Tốt nhất là dùng thư viện sanitize chuyên dụng.

        return `
            <div class="review-item">
                <div class="review-author d-flex justify-content-between">
                    <div class="d-flex">
                        <div class="review-author-avatar">
                            <img src="${review.user.avatar || 'https://homepage.momocdn.net/cinema/momo-cdn-api-220615142913-637909001532744942.jpg'}" alt="">
                        </div>
                        <div class="ms-3">
                            <p class="fw-semibold">${review.user.displayName || 'Người dùng ẩn danh'}</p>
                            <p>⭐️ ${review.rating}/10</p>
                        </div>
                    </div>
                    <div>
                        <p>${formatDate(review.createdAt)}</p>
                    </div>
                </div>
                <div class="review-content mt-3">
                   
                    <p>${safeContent}</p>
                </div>
                <div class="review-actions mt-2">
                    ${actionButtonsHTML} 
                </div>
            </div>
        `;
    }).join('');
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
isUpdating = false;

// Hàm lấy reviews (sử dụng async/await, arrow function và try/catch)
const getReviews = async (page) => {
    try {
        const movieId = movie.id;
        const response = await axios.get('/api/reviewss/get', {
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

    // === Thêm kiểm tra rating ===
    if (currentRating === 0) {
        alert("Vui lòng chọn số sao đánh giá.");
        return;
    }
    // === Kết thúc kiểm tra rating ===

    const content = reviewContentEl.value.trim();
    if (!content) {
        alert("Vui lòng nhập nội dung");
        reviewContentEl.focus(); // Focus vào textarea
        return;
    }

    const payload = {
        content: content,
        rating: currentRating,
        movieId: movie.id,
    };

    const loginPageUrl = '/dangnhap-dangky'; // <-- Đảm bảo URL này đúng

    try {
        await axios.post('/api/reviews', payload);
        alert("Tạo review thành công");

        // Đóng modal và reset
        const modalEl = document.getElementById('modal-review');
        const modalInstance = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
        modalInstance.hide(); // Đóng modal

        reviewForm.reset(); // Reset form
        currentRating = 0;  // Reset rating state
        resetStars();       // Reset UI sao
        if (ratingValue) ratingValue.textContent = ""; // Xóa text rating

        getReviews(1); // Tải lại review

    } catch (error) {
        console.error("Lỗi khi tạo review:", error); // Log lỗi chi tiết cho dev

        // --- Xử lý lỗi 401 ---
        if (error.response && error.response.status === 401) {
            // Đóng modal trước khi hiển thị confirm (để tránh modal che confirm)
            const modalEl = document.getElementById('modal-review');
            const modalInstance = bootstrap.Modal.getInstance(modalEl);
            if (modalInstance) {
                // Cần đợi modal ẩn hoàn toàn trước khi confirm để tránh lỗi UI
                modalEl.addEventListener('hidden.bs.modal', () => {
                    if (window.confirm("Phiên đăng nhập của bạn đã hết hạn hoặc không hợp lệ. Bạn có muốn đăng nhập lại không?")) {
                        window.location.href = loginPageUrl;
                    }
                }, { once: true }); // Chỉ chạy listener này 1 lần
                modalInstance.hide();
            } else {
                // Nếu không tìm thấy modal instance, hiển thị confirm ngay
                if (window.confirm("Phiên đăng nhập của bạn đã hết hạn hoặc không hợp lệ. Bạn có muốn đăng nhập lại không?")) {
                    window.location.href = loginPageUrl;
                }
            }

            // Reset form và trạng thái ngay cả khi người dùng không chuyển trang
            reviewForm.reset();
            currentRating = 0;
            resetStars();
            if (ratingValue) ratingValue.textContent = "";

        } else {
            // --- Xử lý các lỗi khác ---
            const errorMessage = error.response?.data?.message // Lấy thông báo lỗi từ backend (nếu có)
                || error.message             // Hoặc lấy thông báo lỗi mặc định
                || "Đã có lỗi xảy ra khi gửi đánh giá."; // Thông báo chung
            alert("Lỗi: " + errorMessage);
            // Giữ modal mở để người dùng có thể sửa lại hoặc thử lại
        }
    }
}

// --- Cần đảm bảo listener submit gọi hàm này ---
// reviewForm.removeEventListener("submit", handleFormSubmit); // Xóa listener cũ nếu có
// reviewForm.addEventListener("submit", handleCreateReview); // Thêm listener này (chỉ khi dùng cách 1)
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

// Hàm xử lý khi nhấn nút sửa (không cần thay đổi nhiều, chỉ cần đảm bảo nhận đúng content)
async function handleEditReviewClick(event, reviewId, reviewContent, reviewRating) {
    // reviewContent giờ đây là một chuỗi JS hợp lệ (nhờ JSON.stringify)
    reviewContentEl.value = reviewContent; // Gán trực tiếp vào textarea
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

    // Loại bỏ event listener cũ và thêm event listener mới (logic này có vẻ hơi phức tạp, xem xét lại nếu cần)
    // Cách làm này có thể dẫn đến việc listener bị gắn nhiều lần nếu modal đóng mở không đúng cách.
    // Một cách tiếp cận khác là luôn giữ listener `handleFormSubmit` và chỉ thay đổi cờ `isUpdating`.
    reviewForm.removeEventListener("submit", handleFormSubmit); // Xóa listener cũ (nếu có)
    reviewForm.addEventListener("submit", handleFormSubmit);    // Thêm listener mới
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
