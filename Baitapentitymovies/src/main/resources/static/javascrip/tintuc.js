document.addEventListener("DOMContentLoaded", () => {
    const tocLinks = document.querySelectorAll(".toc-container a");

    tocLinks.forEach(link => {
        link.addEventListener("click", () => {
            // Xóa lớp 'active' khỏi tất cả các liên kết
            tocLinks.forEach(link => link.classList.remove("active"));

            // Thêm lớp 'active' vào liên kết được nhấn
            link.classList.add("active");
        });
    });
});
document.addEventListener("DOMContentLoaded", () => {
    const tocLinks = document.querySelectorAll(".toc-container a");
    const sections = Array.from(tocLinks).map(link => document.querySelector(link.getAttribute("href")));

    // Xử lý sự kiện click để làm nổi bật mục được nhấn
    tocLinks.forEach(link => {
        link.addEventListener("click", () => {
            tocLinks.forEach(link => link.classList.remove("active"));
            link.classList.add("active");
        });
    });

    // Xử lý sự kiện cuộn để làm nổi bật mục lục tương ứng
    window.addEventListener("scroll", () => {
        let currentSection = null;

        sections.forEach((section, index) => {
            const sectionTop = section.offsetTop - 100; // Điều chỉnh khoảng cách
            const sectionBottom = sectionTop + section.offsetHeight;

            if (window.scrollY >= sectionTop && window.scrollY < sectionBottom) {
                currentSection = tocLinks[index];
            }
        });

        // Cập nhật trạng thái active trong mục lục
        tocLinks.forEach(link => link.classList.remove("active"));
        if (currentSection) {
            currentSection.classList.add("active");
        }
    });
});