# Scala-IRC-bot

This is a Scala implementation of an IRC bot, on top of https://github.com/pircbotx/pircbotx.

## How to install
First, make a configuration file, of this format:

```
bot-configuration {
  connection {
    server-name = "irc.someserver.com"
    port = 6697
    ssl = true
  }

  nickname = "Bot nickname"
  nickserv-password = "Bot's nickserv pass" // Optional
  channels = ["#array", "#of", "#channels"]
  finger-msg = "Some message" // Optional
  bot-admins = ["Array", "of", "nicknames"]
  listeners = ["adminListener", "linkListener"]
  help-text = "Some help text"

}
```

Use `sbt assembly` to create a .jar. Then run the bot with `java -jar <filename.jar> <configfile>`.

In the configuration file, the array of `bot-admins` is used to decide who is allowed to run admin-only commands (currently
 only !quit in the adminListener). The `listeners` array is the most important. This decides which functionality your
 instance of the bot will have.
 
## Available listeners

### adminListener
This should always be on. It sets the +B (bot user mode that many IRC servers require) on connect, and it listens to
!help messages, and !quit messages from bot-admins.

### linkListener
This listener reacts to messages and actions containing http/https links. It attempts to retrieve the <title> tag in
html pages and if it can find one, it will send the title to the channel.