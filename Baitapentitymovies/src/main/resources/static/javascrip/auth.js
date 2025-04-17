document.addEventListener("DOMContentLoaded", () => {
    const navLoginLink = document.querySelector(".nav-link.ju");
    const isLoggedIn = sessionStorage.getItem("isLoggedIn") === "true";

    if (isLoggedIn && navLoginLink) {
        navLoginLink.textContent = "Đăng xuất";
        navLoginLink.href = "#";
        navLoginLink.addEventListener("click", (e) => {
            e.preventDefault();
            sessionStorage.removeItem("isLoggedIn");
            alert("Đăng xuất thành công!");
            location.reload(); // Tải lại để đổi lại nút thành "Đăng nhập"
        });
    }
});
