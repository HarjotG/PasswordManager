# PasswordManager
A secure android password manager app that stores username/password account information in an SQLite database.

The passwords are encrypted before they are stored in the database using AES encryption.

The encryption keys are securely stored in the android keystore to mitigate against an attacker trying to steal the key.

