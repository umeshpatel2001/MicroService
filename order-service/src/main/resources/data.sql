INSERT INTO orders (order_status, total_price)
VALUES ('CONFIRMED', 1200.50),
       ('PENDING', 850.00),
       ('CANCELED', 450.75),
       ('CONFIRMED', 999.99),
       ('PENDING', 300.20),
       ('CONFIRMED', 1500.00),
       ('CANCELED', 700.00),
       ('CONFIRMED', 200.99),
       ('PENDING', 780.45),
       ('CONFIRMED', 560.10);

INSERT INTO order_item (product_id, quantity, order_id)
VALUES (1, 2, 1),
       (2, 1, 1),

       (3, 3, 2),
       (4, 1, 2),

       (5, 2, 3),
       (6, 1, 3),

       (7, 1, 4),
       (8, 2, 4),

       (9, 4, 5),
       (10, 1, 5),

       (11, 2, 6),
       (12, 3, 6),

       (13, 1, 7),
       (14, 2, 7),

       (15, 1, 8),
       (16, 1, 8),

       (17, 3, 9),
       (18, 2, 9),

       (19, 1, 10),
       (20, 2, 10);