/**
 * Keylocks is an app to handle the creation and storage of PGP keys. It uses Minigmand.
 *
 * Keylocks does three things: it imports existing PGP keys, both private and public.
 * it creates new key pairs.
 * it generates and teaches random-word passphrases and applies them to existing or new pgp private keys.
 *
 *
 * It will provide an Intent driven  pgp-based crypto api to other apps
 *
 *
 * supporting the generation and use of random-word passphrases, of the
 * type made famous by this XKCD cartoon: https://www.xkcd.com/936/ . See also https://en.wikipedia.org/wiki/Diceware
 * although Passphrases uses by default the system  pseudo-random number generator (java.security.SecureRandom) rather than hardware
 *  dice.
 *
 *  Passphrases uses the Electronic Frontier Foundation's wordlists through the associated library Effwords. These
 *  wordlists are currently only supported in English.
 *
 *  As well as generating the passphrase, Passphrases provides a flashcard sequence to help the user learn and remember
 *  the phrase without writing it down.
 *
 */
package uk.co.platosys.keylocks;
