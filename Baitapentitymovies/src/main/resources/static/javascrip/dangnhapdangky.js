// --- Cấu hình Axios Interceptor (ĐẶT Ở ĐẦU FILE HOẶC FILE CONFIG RIÊNG) ---
axios.interceptors.request.use(
    config => {
        const token = localStorage.getItem('jwtToken');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// Optional: Interceptor để xử lý lỗi 401 (Unauthorized) toàn cục
axios.interceptors.response.use(
    response => response, // Chỉ trả về response nếu không có lỗi
    error => {
        if (error.response && error.response.status === 401) {
            // Xử lý khi token không hợp lệ hoặc hết hạn
            console.error("Unauthorized request or token expired:", error.response.data);
            localStorage.removeItem('jwtToken'); // Xóa token hỏng/hết hạn
            alert("Phiên đăng nhập của bạn đã hết hạn hoặc không hợp lệ. Vui lòng đăng nhập lại.");
            // Điều hướng về trang đăng nhập
            // Đảm bảo không tạo vòng lặp vô hạn nếu trang đăng nhập cũng dùng interceptor này
            if (window.location.pathname !== "/dangnhap-dangky") { // Thay bằng URL trang đăng nhập của bạn
                window.location.href = "/dangnhap-dangky"; // URL trang đăng nhập
            }
        }
        return Promise.reject(error); // Chuyển lỗi đi để các catch khác có thể xử lý
    }
);

document.addEventListener("DOMContentLoaded", function () {

    // --- Các phần tử và URL quan trọng ---
    // Phần tử Navbar (sẽ chỉ tồn tại sau khi đăng nhập và render bởi Thymeleaf)
    const logoutLink = document.getElementById('logout-link'); // ID này cần khớp với thẻ <a> đăng xuất trong template Thymeleaf của bạn

    // Phần tử trên trang đăng nhập/đăng ký
    const loginForm = document.getElementById('login-form'); // Form đăng nhập
    const loginEmailInput = document.getElementById('login-email'); // Input email đăng nhập
    const loginPasswordInput = document.getElementById('login-password'); // Input mật khẩu đăng nhập
    // const loginErrorMessage = document.getElementById('login-error-message'); // Nếu bạn thêm div hiển thị lỗi

    const container = document.getElementById('container'); // Container chuyển đổi form
    const registerBtn = document.getElementById('register'); // Nút chuyển sang đăng ký
    const loginBtn = document.getElementById('login');     // Nút chuyển sang đăng nhập (toggle)

    // URLs
    const LOGIN_API_URL = "/api/auth/login";   // URL API Đăng nhập - **Thay đổi nếu cần**
    const LOGOUT_API_URL = "/api/auth/logout"; // URL API Đăng xuất - **Thay đổi nếu cần**
    const HOME_PAGE_URL = "/";                 // URL trang chủ sau khi đăng nhập - **Thay đổi nếu cần**
    // const LOGIN_PAGE_URL = "/dangnhap-dangky"; // URL trang đăng nhập (dùng nếu muốn điều hướng về đây sau logout)

    // --- Hàm xử lý đăng nhập ---
    async function handleLogin(event) {
        event.preventDefault(); // Ngăn form submit theo cách truyền thống
        console.log("Attempting login...");

        // Kiểm tra lại sự tồn tại của input phòng trường hợp trang lỗi
        if (!loginEmailInput || !loginPasswordInput) {
            console.error("Login form input elements not found!");
            alert("Lỗi: Không tìm thấy ô nhập liệu email hoặc mật khẩu.");
            return;
        }

        const email = loginEmailInput.value.trim();
        const password = loginPasswordInput.value.trim();

        // Xóa thông báo lỗi cũ (nếu bạn có div hiển thị lỗi)
        // if (loginErrorMessage) loginErrorMessage.textContent = '';

        if (!email || !password) {
            // if (loginErrorMessage) loginErrorMessage.textContent = "Vui lòng nhập đầy đủ email và mật khẩu.";
            alert("Vui lòng nhập đầy đủ email và mật khẩu."); // Dùng alert nếu không có div lỗi
            return;
        }

        const payload = {
            email: email,
            password: password
        };

        // Vô hiệu hóa nút submit để tránh click nhiều lần
        const submitButton = loginForm.querySelector('button[type="submit"]');
        if(submitButton) submitButton.disabled = true;
        if(submitButton) submitButton.textContent = 'Đang xử lý...'; // Thay đổi text nút (tùy chọn)


        try {
            const response = await axios.post(LOGIN_API_URL, payload);

            // QUAN TRỌNG: LƯU TOKEN
            if (response.data && response.data.token) {
                localStorage.setItem('jwtToken', response.data.token); // Lưu vào Local Storage
                // Hoặc sessionStorage.setItem('jwtToken', response.data.token); nếu muốn token mất khi đóng tab
                console.log("Login successful, token stored:", response.data.token);
                alert("Đăng nhập thành công!");

                // Điều hướng về trang chủ
                window.location.href = HOME_PAGE_URL;
            } else {
                console.error("Login successful, but no token received in response:", response.data);
                alert("Đăng nhập thành công nhưng không nhận được token xác thực.");
            }
        } catch (error) {
            console.error("Login Error:", error);
            let errorMessage = "Đăng nhập thất bại!";

            if (error.response) {
                // Lỗi có phản hồi từ server (vd: sai mật khẩu, 401, 400)
                errorMessage = error.response.data?.message || `Lỗi ${error.response.status}: Thông tin đăng nhập không chính xác hoặc tài khoản không tồn tại.`;
                console.error("Login Server Error Data:", error.response.data);
            } else if (error.request) {
                // Lỗi mạng, không nhận được phản hồi
                errorMessage = "Không thể kết nối đến máy chủ. Vui lòng kiểm tra mạng và thử lại.";
                console.error("Login Network Error (no response):", error.request);
            } else {
                // Lỗi khác trong quá trình gửi request
                errorMessage = "Đã xảy ra lỗi không mong muốn trong quá trình đăng nhập.";
                console.error("Login Request Setup Error:", error.message);
            }

            // Hiển thị lỗi cho người dùng
            // if (loginErrorMessage) loginErrorMessage.textContent = errorMessage;
            alert(errorMessage); // Dùng alert

            // Bật lại nút submit nếu có lỗi
            if(submitButton) {
                submitButton.disabled = false;
                submitButton.textContent = 'Sign In'; // Khôi phục text gốc
            }
        }
    }

    // --- Hàm xử lý đăng xuất ---
    async function handleLogout(event) {
        event.preventDefault(); // Ngăn thẻ <a> điều hướng
        console.log("Attempting logout...");

        // Có thể thêm confirm dialog (tùy chọn)
        if (!confirm("Bạn có chắc chắn muốn đăng xuất?")) {
            return; // Người dùng hủy
        }

        const token = localStorage.getItem('jwtToken');
        try {
            // Gọi API backend để hủy session/token (nếu backend có hỗ trợ blacklist)
            // Nếu backend không có logic logout đặc biệt cho JWT (chỉ dựa vào client xóa token),
            // bạn có thể bỏ qua bước gọi API này hoặc gọi một endpoint hình thức.
            if (token) { // Chỉ gọi API logout nếu có token
                await axios.post(LOGOUT_API_URL, {}, { // Gửi object rỗng nếu API không cần body
                    headers: {
                        // 'Authorization': `Bearer ${token}` // Gửi token nếu API logout của bạn cần nó để blacklist
                    }
                });
                console.log("Server-side logout (if implemented) successful.");
            }


            // QUAN TRỌNG: XÓA TOKEN KHỎI CLIENT-SIDE
            localStorage.removeItem('jwtToken');
            console.log("Token removed from local storage.");
            alert("Đăng xuất thành công!");

            // Tải lại trang hoặc điều hướng về trang đăng nhập
            window.location.href = "/dangnhap-dangky"; // Điều hướng về trang đăng nhập thường tốt hơn reload
            // window.location.reload();

        } catch (error) {
            console.error("Logout Error:", error);
            let errorMessage = "Đăng xuất thất bại!";
            // ... (phần xử lý lỗi giữ nguyên) ...
            alert(errorMessage);

            // Ngay cả khi logout API thất bại, vẫn nên xóa token phía client để đảm bảo
            localStorage.removeItem('jwtToken');
            console.log("Token removed from local storage despite server error during logout.");
            // Và điều hướng người dùng ra
            if (window.location.pathname !== "/dangnhap-dangky") {
                window.location.href = "/dangnhap-dangky";
            }
        }
    }

    // --- Gắn sự kiện ---

    // 1. Gắn sự kiện cho Form Đăng nhập (CHỈ KHI form tồn tại trên trang hiện tại)
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
        console.log("Login form event listener attached.");

        // Tùy chọn: Xóa thông báo lỗi khi người dùng nhập lại vào các trường
        // if(loginEmailInput && loginErrorMessage) loginEmailInput.addEventListener('input', () => { loginErrorMessage.textContent = ''; });
        // if(loginPasswordInput && loginErrorMessage) loginPasswordInput.addEventListener('input', () => { loginErrorMessage.textContent = ''; });

    } else {
        // Ghi log nếu không tìm thấy form đăng nhập trên trang này (bình thường nếu không ở trang login)
        // console.log("Login form not found on this page.");
    }

    // 2. Gắn sự kiện cho nút/link Đăng xuất (CHỈ KHI link tồn tại - tức là đã đăng nhập)
    if (logoutLink) {
        logoutLink.addEventListener('click', handleLogout);
        console.log("Logout link event listener attached.");
    } else {
        // Ghi log nếu không tìm thấy link logout (bình thường nếu chưa đăng nhập hoặc không ở trang có navbar đầy đủ)
        // console.log("Logout link not found (normal if not logged in or not on a page with the full navbar).");
    }

    // 3. Gắn sự kiện cho các nút chuyển đổi form Đăng nhập/Đăng ký (CHỈ KHI các nút này tồn tại)
    if (container && registerBtn && loginBtn) {
        registerBtn.addEventListener('click', () => {
            container.classList.add("active");
            console.log("Switched to Register view");
            // Có thể xóa lỗi form login cũ khi chuyển đổi (nếu có div lỗi)
            // if (loginErrorMessage) loginErrorMessage.textContent = '';
        });

        loginBtn.addEventListener('click', () => {
            container.classList.remove("active");
            console.log("Switched to Login view");
            // Có thể xóa lỗi form sign up cũ khi chuyển đổi
        });
        console.log("Login/Register form switch listeners attached.");
    } else {
        // Ghi log nếu không tìm thấy các nút chuyển đổi (bình thường nếu không ở trang login/register)
        // console.log("Login/Register toggle elements not found on this page.");
    }

});