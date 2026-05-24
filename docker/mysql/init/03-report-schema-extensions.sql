-- Report-service columns and retort table (not yet in reconV2/schema.sql).
USE recon;

ALTER TABLE report
  ADD COLUMN is_finalized BOOLEAN DEFAULT FALSE,
  ADD COLUMN finalized_at DATETIME NULL;

CREATE TABLE IF NOT EXISTS retort (
  retort_id INTEGER NOT NULL AUTO_INCREMENT,
  trainee_author_id INTEGER,
  content VARCHAR(255),
  created_at DATETIME,
  report_id INTEGER NOT NULL,
  PRIMARY KEY (retort_id),
  UNIQUE KEY uk_retort_report_id (report_id),
  FOREIGN KEY (report_id) REFERENCES report(report_id)
);
