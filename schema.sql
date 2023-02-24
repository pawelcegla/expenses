CREATE TABLE expenses (
  date TEXT NOT NULL CHECK (date(date) IS NOT NULL),
  amount DECIMAL(10,2) NOT NULL,
  description TEXT NOT NULL,
  tags TEXT NOT NULL CHECK (json_valid(tags) AND json_type(tags) = 'array'));
