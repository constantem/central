-- 2. 插入帳號主表 (FX_ACCOUNT)
INSERT INTO FX_ACCOUNT (account_number) VALUES ('00111912224');
INSERT INTO FX_ACCOUNT (account_number) VALUES ('00112111234');
INSERT INTO FX_ACCOUNT (account_number) VALUES ('00112205319');

-- 3. 插入各帳號的幣別餘額 (FX_ACCOUNT_BALANCES)
-- 帳號：00111912224
INSERT INTO FX_ACCOUNT_BALANCES (account_number, currency, balance) VALUES ('00111912224', 'USD', 25090.50);
INSERT INTO FX_ACCOUNT_BALANCES (account_number, currency, balance) VALUES ('00111912224', 'JPY', 350000);
INSERT INTO FX_ACCOUNT_BALANCES (account_number, currency, balance) VALUES ('00111912224', 'CAD', 0);
INSERT INTO FX_ACCOUNT_BALANCES (account_number, currency, balance) VALUES ('00111912224', 'HKD', 0);
INSERT INTO FX_ACCOUNT_BALANCES (account_number, currency, balance) VALUES ('00111912224', 'GBP', 0);

-- 帳號：00112111234
INSERT INTO FX_ACCOUNT_BALANCES (account_number, currency, balance) VALUES ('00112111234', 'USD', 12000.0);
INSERT INTO FX_ACCOUNT_BALANCES (account_number, currency, balance) VALUES ('00112111234', 'JPY', 0);
INSERT INTO FX_ACCOUNT_BALANCES (account_number, currency, balance) VALUES ('00112111234', 'CAD', 87120);
INSERT INTO FX_ACCOUNT_BALANCES (account_number, currency, balance) VALUES ('00112111234', 'HKD', 55432);
INSERT INTO FX_ACCOUNT_BALANCES (account_number, currency, balance) VALUES ('00112111234', 'GBP', 876);

-- 帳號：00112205319
INSERT INTO FX_ACCOUNT_BALANCES (account_number, currency, balance) VALUES ('00112205319', 'USD', 50.75);
INSERT INTO FX_ACCOUNT_BALANCES (account_number, currency, balance) VALUES ('00112205319', 'JPY', 880400);
INSERT INTO FX_ACCOUNT_BALANCES (account_number, currency, balance) VALUES ('00112205319', 'CAD', 970);
INSERT INTO FX_ACCOUNT_BALANCES (account_number, currency, balance) VALUES ('00112205319', 'HKD', 354000);
INSERT INTO FX_ACCOUNT_BALANCES (account_number, currency, balance) VALUES ('00112205319', 'GBP', 750010);

-- 插入匯率假資料 (以 TWD 為基準)
-- 銀行買入 (Buy)：客戶把外幣賣給銀行，換回台幣
-- 銀行賣出 (Sell)：客戶跟銀行買外幣，付出台幣
INSERT INTO FX_RATE (currency, buy_rate, sell_rate) VALUES ('USD', 31.5, 32.1);
INSERT INTO FX_RATE (currency, buy_rate, sell_rate) VALUES ('JPY', 0.21, 0.23);
INSERT INTO FX_RATE (currency, buy_rate, sell_rate) VALUES ('CAD', 22.8, 23.5);
INSERT INTO FX_RATE (currency, buy_rate, sell_rate) VALUES ('HKD', 3.9, 4.2);
INSERT INTO FX_RATE (currency, buy_rate, sell_rate) VALUES ('GBP', 39.5, 40.8);