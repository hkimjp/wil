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

-- :name last-note :? :1
-- :doc  login's last note
SELECT note FROM notes
WHERE login=:login
order by id desc
limit 1
