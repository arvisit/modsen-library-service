INSERT INTO books(id) VALUES
    ('f4b27fe4-0ea1-423d-add6-daa2ee63802f'),
    ('c79eb50b-2e71-4e98-87ab-2074c7441713'),
    ('0319cc17-a6e0-4bfd-a9ef-2ab5cca8abca');

INSERT INTO book_records(book_id, borrower_username, borrowed_date, return_due_date, returned_date) VALUES
    ('c79eb50b-2e71-4e98-87ab-2074c7441713', 'user@mail.com', '2020-01-01', '2020-02-01', '2020-01-15'),
    ('0319cc17-a6e0-4bfd-a9ef-2ab5cca8abca', 'user@mail.com', '2020-01-01', '2020-02-01', '2020-01-15'),
    ('0319cc17-a6e0-4bfd-a9ef-2ab5cca8abca', 'user@mail.com', '2021-01-01', '2021-02-01', null);
