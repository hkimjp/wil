-- -- :name create-user! :! :n
-- -- :doc creates a new user record
-- INSERT INTO users
-- (id, first_name, last_name, email, pass)
-- VALUES (:id, :first_name, :last_name, :email, :pass)

-- -- :name update-user! :! :n
-- -- :doc updates an existing user record
-- UPDATE users
-- SET first_name = :first_name, last_name = :last_name, email = :email
-- WHERE id = :id

-- -- :name get-user :? :1
-- -- :doc retrieves a user record given the id
-- SELECT * FROM users
-- WHERE id = :id

-- -- :name delete-user! :! :n
-- -- :doc deletes a user record given the id
-- DELETE FROM users
-- WHERE id = :id

-- :name create-date! :! :n
-- :doc seed date in weeks table
INSERT INTO weeks
(wday, date)
VALUES (:wday, :date)

-- :name create-note! :! :n
-- :doc  creates users new note
INSERT INTO notes
(login, date, note)
VALUES (:login, :date, :note)

-- :name notes-login :? :*
-- :doc  retrieve login's notes
SELECT * FROM notes
WHERE login = :login
ORDER BY id

-- :name date-count :? :*
-- :doc  how many wils in same day with login?
SELECT date, count(*) FROM notes
GROUP BY date

-- :name notes-all :? :*
-- :doc retrieve all notes
SELECT * FROM notes
ORDER BY id

-- :name get-note :? :1
-- :doc  get note id=:id
SELECT * FROM notes
WHERE id = :id
ORDER BY id

-- :name date-notes-randomly :? :*
-- :doc retrieve n notes randomly
SELECT * FROM notes
WHERE date = :date
ORDER BY RANDOM()
LIMIT :n

-- :name list-notes :? :*
-- :doc retrieve date's all notes
SELECT * FROM notes
WHERE date = :date

-------------------------------------
-- goods table
-- :name find-good-bad :? :1
SELECT * from goods
WHERE from_ = :from_ and to_ = :to_

-- :name create-good-bad! :! :n
-- :doc  create goods params from_, to_, kind
INSERT INTO goods
(from_, to_, kind)
VALUES (:from_, :to_, :kind)

-- :name good-bad :? :*
-- :doc retrieve goods and bads sent to id
SELECT * from goods
WHERE to_= :id

-- :name good-bad-sent :? :*
-- :doc retrieve goods and bads sent from login
SELECT * from goods
WHERE from_= :login

-- :name goods-bads :? :1
-- :doc retrieve goods/so-so/bads count for date
SELECT count(*) from goods
WHERE date(created_at) = date(:date) and kind=:kind;

-------------------------------------
-- corona table
-- :name clear-corona! :! :n
-- :doc  clear corona table
DELETE FROM corona

-- :name insert-corona! :! :n
-- :doc  insert into corona table sid, date
INSERT INTO corona
(sid, date)
VALUES
(:sid, :date)

-- :name list-corona :? :*
-- :doc  fetch all data from corona table
SELECT * from corona
