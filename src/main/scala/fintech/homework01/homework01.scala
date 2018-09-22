package fintech.homework01

// Используя функции io.readLine и io.printLine напишите игру "Виселица"
// Пример ввода и тест можно найти в файле src/test/scala/fintech/homework01/HangmanTest.scala
// Тест можно запустить через в IDE или через sbt (написав в консоли sbt test)

// Правила игры "Виселица"
// 1) Загадывается слово
// 2) Игрок угадывает букву
// 3) Если такая буква есть в слове - они открывается
// 4) Если нет - рисуется следующий элемент висельника
// 5) Последней рисуется "веревка". Это означает что игрок проиграл
// 6) Если игрок все еще жив - перейти к пункту 2

// Пример игры:

// Word: _____
// Guess a letter:
// a
// Word: __a_a
// Guess a letter:
// b
// +----
// |
// |
// |
// |
// |

// и т.д.

class Hangman(io: IODevice) {
  private var hiddenWord = ""
  private var currentStage = -1
  private var continue = true

  def play(word: String): Unit = {
    hiddenWord = "_" * word.length

    do {
      io.printLine("Word: " + hiddenWord)
      io.printLine("Guess a letter:")

      val userLetter = io.readLine().charAt(0)
      val foundIndices = word.zipWithIndex.filter(_._1 == userLetter).map(_._2)

      if (foundIndices.nonEmpty) {
        userRight(foundIndices, userLetter)
      } else {
        userWrong()
      }
    } while (continue)
  }

  def userRight(foundIndices: IndexedSeq[Int], userLetter: Char): Unit = {
    foundIndices.foreach(i =>
      hiddenWord = hiddenWord.updated(i, userLetter)
    )

    if (currentStage >= 0)
      io.printLine(stages(currentStage))

    if (!hiddenWord.contains("_")) {
      io.printLine("Word: " + hiddenWord)
      io.printLine("You win!")
      continue = false
    }
  }

  def userWrong(): Unit = {
    currentStage += 1
    io.printLine(stages(currentStage))

    if (currentStage == stages.length - 1) {
      io.printLine("You are dead")
      continue = false
    }
  }

  val stages = List(
    """+----
      ||
      ||
      ||
      ||
      ||
      |""".stripMargin,
    """+----
      ||
      ||   O
      ||
      ||
      ||
      |""".stripMargin,
    """+----
      ||
      ||   O
      ||   |
      ||
      ||
      |""".stripMargin,
    """+----
      ||
      ||   O
      ||   |
      ||  /
      ||
      |""".stripMargin,
    """+----
      ||
      ||   O
      ||   |
      ||  / \
      ||
      |""".stripMargin,
    """+----
      ||
      ||   O
      ||  /|
      ||  / \
      ||
      |""".stripMargin,
    """+----
      ||
      ||   O
      ||  /|\
      ||  / \
      ||
      |""".stripMargin,
    """+----
      ||   |
      ||   O
      ||  /|\
      ||  / \
      ||
      |""".stripMargin
  )
}

trait IODevice {
  def printLine(text: String): Unit
  def readLine(): String
}
