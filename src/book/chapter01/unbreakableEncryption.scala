package book.chapter01.unbreakableEncryption

import scodec.bits.ByteVector

import java.security.SecureRandom
import java.nio.charset.CharacterCodingException

case class KeyPair(
  key1: ByteVector,
  key2: ByteVector
) {
  def decrypt: Either[CharacterCodingException, String] = {
    val decrypted = key1 ^ key2

    decrypted.decodeUtf8
  }
}

object KeyPair {
  def randomKey(length: Long): ByteVector = {
    val rng = SecureRandom()

    var dummy = ByteVector.fill(length) { 0 }.toArray

    rng.nextBytes(dummy)

    ByteVector(dummy)
  }

  def encrypt(s: String): KeyPair = {
    val originalAsBytes = ByteVector(s.getBytes.nn)

    val dummyKey = KeyPair.randomKey(originalAsBytes.length)

    val encryptedKey = originalAsBytes ^ dummyKey

    KeyPair(
      key1 = dummyKey,
      key2 = encryptedKey
    )
  }
}

@main def run(): Unit = {
  val str = "My super secret password abc890xyz123 🔐"
  val kp = KeyPair.encrypt(str)

  kp.decrypt match {
    case Left(err) => {
      println("Oops! Ran into an error while decrypting the string.")
    }

    case Right(decrypted) => {
      println(s"Decrypted: `$decrypted`")

      println("But are they the same?!")

      println(
        if str.contentEquals(decrypted) then "Of course they are!" else "Oh no."
      )
    }
  }
}
