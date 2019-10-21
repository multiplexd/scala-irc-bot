package codes.co2.ircbot.config

import java.nio.file.Path

import org.slf4j.{Logger, LoggerFactory}
import pureconfig.ConfigSource
import pureconfig.generic.auto._ // IntelliJ might see this as an unused import. IntelliJ is wrong.

case class BotConfiguration(
                             connection: Connection,
                             nickname: String,
                             realname: Option[String],
                             nickservPassword: Option[String],
                             channels: Seq[String],
                             fingerMsg: Option[String],
                             listeners: Seq[String],
                             ignore: Option[Seq[String]],
                           )

case class Connection(serverName: String, port: Int, ssl: Boolean)

trait ListenerConfig {
  val ignoreChannels: Option[Seq[String]]
  val ignoredChannels: Seq[String] = ignoreChannels.getOrElse(Seq.empty)
}

case class LinkListenerConfig(
                               boldTitles: Option[Boolean],
                               ignoreChannels: Option[Seq[String]]) extends ListenerConfig

case class AdminListenerConfig(
                                helpText: String,
                                botAdmins: Seq[String],
                                puppetMasters: Option[Seq[String]],
                                ignoreChannels: Option[Seq[String]]) extends ListenerConfig

object BotConfiguration {
  val log: Logger = LoggerFactory.getLogger(getClass)

  def loadConfig(path: Path): BotConfiguration = ConfigSource.default(ConfigSource.file(path))
    .at("bot-configuration").load[BotConfiguration]
    .fold(failures => throw new Exception(failures.toString), success => success)

  def loadLinkListenerConfig(path: Path): LinkListenerConfig = ConfigSource.default(ConfigSource.file(path))
    .at("link-listener").load[LinkListenerConfig]
    .fold(failures => {
      log.info(s"Could not load admin-listener config, reason ${failures.toList.map(_.description)} Using default config.")
      LinkListenerConfig(None, None)
    }, success => success)

  def loadAdminListenerConfig(path: Path): AdminListenerConfig = ConfigSource.default(ConfigSource.file(path))
    .at("admin-listener").load[AdminListenerConfig]
    .fold(failures => {
      log.info(s"Could not load admin-listener config, reason ${failures.toList.map(_.description)} Using default config.")
      AdminListenerConfig("", Seq.empty, None, None)
    }, success => success)


}
