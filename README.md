1. Chức năng tìm kiếm sản phẩm
Người dùng có thể nhập từ khóa tìm kiếm trên giao diện.
Hệ thống sẽ nhận keyword từ @RequestParam trong ProductController và lọc sản phẩm theo tên.
Kết quả tìm kiếm sẽ được hiển thị trên cùng trang index.html.
2. Chức năng xem chi tiết sản phẩm
Khi người dùng nhấp vào một sản phẩm, hệ thống sẽ điều hướng đến /id tương ứng.
Controller lấy dữ liệu sản phẩm theo id và hiển thị nó trong product.html.
3. Chức năng lọc sản phẩm theo giá
Người dùng có thể lọc sản phẩm dựa trên khoảng giá (minPrice và maxPrice).
Controller xử lý lọc giá và sử dụng redirect để điều hướng đến trang /filter-result.
Dữ liệu sau lọc được hiển thị trên filtered-products.html.
4. Quản lý sản phẩm và phân trang
Dữ liệu sản phẩm được lấy từ ProductService, với hỗ trợ phân trang.
Kết quả được hiển thị dưới dạng danh sách, mỗi trang chứa tối đa 10 sản phẩm.![image](https://github.com/user-attachments/assets/5010dd73-2749-477a-894f-5a19884f2713)
![image](https://github.com/user-attachments/assets/22ecc44d-0a32-4eff-8370-4a59d4697143)
