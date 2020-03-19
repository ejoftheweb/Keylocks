# Keylocks
Yet another Android PGP key management tool. Using the Minigma(nd) library which itself is built on BouncyCastle. Very early stages atm

Mostly-working features
-
- integrated random-word passphrase system using the EFF wordlist and predictive-text input widget
- key-pair generation
- identity-binding to existing Profile ids

Features in development:
- 
- key binding to multiple social-media ids with Google authentication api.
- public key storage integrated with Android contacts

Planned features: 
-
- passphrase short-term cache protected by biometrics on compatible devices
- signature and encryption api available to other apps.
- pluggable wordlists for the random-word passphrase system
