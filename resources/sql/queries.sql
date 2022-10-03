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

-- :name user-notes :? :*
-- :doc  retrieve login's notes
SELECT * FROM notes
WHERE login = :login

-- :name date-notes :? :*
-- :doc  retrieve date's notes
SELECT * FROM notes
WHERE date = :date

-- :name find-note :? :*
-- :doc find login's note on date
SELECT * FROM notes
WHERE login = :login and date = :date

-- :name get-note :? :1
-- :doc  get note id=:id
SELECT * FROM notes
WHERE id = :id
