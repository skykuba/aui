-- Create databases if they don't exist
CREATE DATABASE client_db;
CREATE DATABASE invoice_db;

-- Grant privileges to user
GRANT ALL PRIVILEGES ON DATABASE client_db TO "user";
GRANT ALL PRIVILEGES ON DATABASE invoice_db TO "user";
GRANT CREATE ON DATABASE client_db TO "user";
GRANT CREATE ON DATABASE invoice_db TO "user";

