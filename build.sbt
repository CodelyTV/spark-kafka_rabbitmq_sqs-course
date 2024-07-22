Settings.settings

libraryDependencies := Dependencies.all

SbtAliases.aliases.flatMap { case (alias, command) =>
  addCommandAlias(alias, command)
}

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case PathList("org", "apache", "spark", "unused", "UnusedStubClass.class") =>
    MergeStrategy.first
  case _ => MergeStrategy.first
}
