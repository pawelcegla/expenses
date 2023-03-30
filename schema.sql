CREATE TABLE expenses (
  date TEXT NOT NULL CHECK (date(date) IS NOT NULL) DEFAULT (date('now')),
  amount DECIMAL(10,2) NOT NULL,
  description TEXT NOT NULL,
  tags TEXT NOT NULL CHECK (json_valid(tags) AND json_type(tags) = 'array') DEFAULT '[]');

CREATE VIRTUAL TABLE fts_expenses USING fts5(description, tags, content = expenses, content_rowid = rowid);

CREATE TRIGGER expenses_ai AFTER INSERT ON expenses BEGIN
  INSERT INTO fts_expenses(rowid, description, tags) VALUES (new.rowid, new.description, new.tags);
END;
CREATE TRIGGER expenses_ad AFTER DELETE ON expenses BEGIN
  INSERT INTO fts_expenses(fts_expenses, rowid, description, tags) VALUES ('delete', old.rowid, old.description, old.tags);
END;
CREATE TRIGGER expenses_au AFTER UPDATE ON expenses BEGIN
  INSERT INTO fts_expenses(fts_expenses, rowid, description, tags) VALUES ('delete', old.rowid, old.description, old.tags);
  INSERT INTO fts_expenses(rowid, description, tags) VALUES (new.rowid, new.description, new.tags);
END;

CREATE TABLE current_date (date TEXT NOT NULL CHECK (date(date) IS NOT NULL));
