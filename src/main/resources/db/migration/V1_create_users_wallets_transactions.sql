CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
)

CREATE TABLE wallet (
    wallet_id UUID PRIMARY KEY,
    owner_id UUID REFERENCES users(user_id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    join_code VARCHAR(8) UNIQUE NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transactions (
    transaction_id UUID PRIMARY KEY,
    wallet_id UUID REFERENCES wallet(wallet_id) ON DELETE CASCADE,
    amount NUMERIC(12, 2) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    category VARCHAR(50) NOT NULL,
    updated_by UUID REFERENCES users(user_id) ON DELETE SET NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL DEFAULT 1
);