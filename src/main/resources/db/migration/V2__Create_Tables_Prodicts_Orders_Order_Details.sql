CREATE TABLE products(
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock BIGINT NOT NULL DEFAULT 0,
    author BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_products_author_users_id FOREIGN KEY (author) REFERENCES users(id) ON DELETE NO ACTION
)ENGINE = InnoDB;

CREATE TABLE orders(
    id BIGINT NOT NULL AUTO_INCREMENT,
    date_order TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    total DECIMAL(10,2),
    status ENUM('CREATED','SEND','DELIVERED','CANCELED','HANDBACK'),
    PRIMARY KEY(id),
    CONSTRAINT fk_orders_user_details_id_user_details FOREIGN KEY (user_id) REFERENCES user_details(id) ON DELETE CASCADE
)ENGINE = InnoDB;

CREATE TABLE order_details(
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity BIGINT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    PRIMARY KEY(id),
    CONSTRAINT fk_order_id_orders FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_product_id_products FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE = InnoDB;