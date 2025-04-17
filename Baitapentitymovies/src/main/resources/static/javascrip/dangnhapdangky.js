document.addEventListener("DOMContentLoaded", function () {
    // --- Phần tử trên trang hiện tại ---
    const signInForm = document.querySelector(".sign-in form"); // Có thể null nếu không ở trang login
    const loginButton = signInForm?.querySelector("button");     // Dùng optional chaining (?.)
    const loginRegisterContainer = document.getElementById('container'); // Hoặc một ID/class đặc trưng khác của trang login/register

    // --- Phần tử navbar (luôn có) ---
    const navLoginLink = document.querySelector(".nav-link.ju");

    // --- Kiểm tra xem có đang ở trang đăng nhập/đăng ký không ---
    // Cách 1: Dựa vào sự tồn tại của phần tử đặc trưng
    const isOnLoginPage = !!loginRegisterContainer; // True nếu phần tử tồn tại

    // Cách 2: Dựa vào URL (thay '/login-register.html' bằng path đúng)
    // const loginRegisterPath = '/login-register.html';
    // const isOnLoginPage = window.location.pathname === loginRegisterPath;

    // --- Hàm xử lý đăng nhập (chỉ hoạt động nếu ở trang login) ---
    async function handleLogin(event) {
        event.preventDefault();

        // Chỉ thực hiện nếu form đăng nhập tồn tại
        if (!signInForm) return;

        const email = signInForm.querySelector('input[type="email"]').value.trim();
        const password = signInForm.querySelector('input[type="password"]').value.trim();

        if (!email || !password) {
            alert("Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        const payload = { email: email, password: password };

        try {
            // Giả sử bạn dùng axios
            await axios.post("/api/auth/login", payload);
            sessionStorage.setItem("isLoggedIn", "true");
            alert("Đăng nhập thành công!");
            window.location.href = "/"; // Chuyển về trang chủ
        } catch (error) {
            alert("Đăng nhập thất bại: " + (error.response?.data?.message || "Đã xảy ra lỗi"));
            console.error("Login Error:", error);
        }
    }

    // --- Hàm xử lý đăng xuất (có thể gọi từ bất kỳ trang nào) ---
    async function handleLogout(event) {
        
        event.preventDefault(); // Ngăn thẻ <a> điều hướng
        console.log("Attempting logout..."); // Debug log

        try {
            // 1. Gọi API backend để hủy session/token phía server
            await axios.post("/api/auth/logout"); // <--- Thêm dòng này (hoặc bỏ comment)
            console.log("Server logout request successful."); // Log tùy chọn

            // 2. Nếu API thành công (không ném lỗi), xóa trạng thái ở client
            sessionStorage.removeItem("isLoggedIn");
            alert("Đăng xuất thành công!");

            // 3. Cập nhật lại UI navbar ngay lập tức
            updateLoginNavUI();

            // 4. Chuyển hướng về trang chủ hoặcn trang logi
            window.location.href = "/"; // Hoặc trang login nếu muốn

        } catch (error) {
            // Xử lý lỗi nếu gọi API thất bại hoặc có lỗi khác
            alert("Đăng xuất thất bại! " + (error.response?.data?.message || "Đã xảy ra lỗi phía client."));
            console.error("Logout Error:", error);

            // Cân nhắc: Dù API lỗi, có thể bạn vẫn muốn xóa trạng thái client
            // để tránh người dùng bị kẹt ở trạng thái đăng nhập trên giao diện.
            // Tuy nhiên, điều này có thể che giấu lỗi từ server.
            // Quyết định này tùy thuộc vào luồng xử lý mong muốn.
            // Ví dụ: Vẫn xóa và reload
            /*
            sessionStorage.removeItem("isLoggedIn");
            updateLoginNavUI(); // Cập nhật UI trước khi reload
            location.reload(); // Reload để đảm bảo trạng thái sạch
            */
            // Hoặc chỉ thông báo lỗi và không làm gì thêm
        }
    }

    // --- Hàm cập nhật UI nút Đăng nhập/Xuất trên navbar ---
    function updateLoginNavUI() {
        const isLoggedIn = sessionStorage.getItem("isLoggedIn") === "true";

        if (!navLoginLink) {
            console.error("Navbar login link not found!");
            return;
        }

        // Gỡ bỏ listener cũ trước khi thêm mới để tránh trùng lặp
        navLoginLink.removeEventListener("click", handleLogout);
        // Cũng nên gỡ listener login nếu có (dù ít khả năng bị gắn vào đây)
        // navLoginLink.removeEventListener("click", handleLogin);

        if (isLoggedIn && !isOnLoginPage) {
            // Đã đăng nhập VÀ KHÔNG ở trang login/register
            navLoginLink.textContent = "Đăng xuất";
            navLoginLink.href = "#"; // Ngăn điều hướng mặc định
            navLoginLink.addEventListener("click", handleLogout); // Gắn sự kiện đăng xuất
            console.log("Navbar: Set to Logout button"); // Debug log
        } else {
            // Chưa đăng nhập HOẶC đang ở trang login/register
            navLoginLink.textContent = "Đăng nhập";
            navLoginLink.href = "/dangnhap-dangky"; // Thay bằng path đúng đến trang login
            // Không cần gắn listener 'click' ở đây, vì thẻ <a> sẽ tự điều hướng
            console.log("Navbar: Set to Login button/link"); // Debug log
        }
    }


    // --- Khởi chạy và Gắn sự kiện ---

    // 1. Gắn sự kiện cho nút đăng nhập trên FORM (nếu tồn tại)
    if (loginButton) {
        loginButton.addEventListener("click", handleLogin);
    }

    // 2. Cập nhật trạng thái nút trên NAVBAR ngay khi trang tải
    updateLoginNavUI();

});

// --- Phần xử lý chuyển đổi form đăng nhập/đăng ký (giữ nguyên nếu cần) ---
const container = document.getElementById('container');
const registerBtn = document.getElementById('register');
const loginBtn = document.getElementById('login');

if (container && registerBtn && loginBtn) { // Chỉ chạy nếu các nút này tồn tại
    registerBtn.addEventListener('click', () => {
        container.classList.add("active");
    });

    loginBtn.addEventListener('click', () => {
        container.classList.remove("active");
    });
}