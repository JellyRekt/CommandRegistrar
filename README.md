# CommandTree
CommandTree is a simple way to register your long, multi-token commands and subcommands,
without building a jungle of if-else statements.

It can take your command registration from this:
```java
public boolean execute(CommandSender sender, String command, String label, String[] args) {
    if (args.length <= 0) {
        sender.sendMessage("/team - Display this message");
        return true;
    }
    if (args[0].equals("create")) {
        if (args.length <= 1) {
            sender.sendMessage("/team create <name> - Create a team");
            return true;
        }
        sender.sendMessage(String.format("Created team %s!", args[1]));
        return true;
    }
    if (args[0].equals("join")) {
        if (args.length <= 1) {
            sender.sendMessage("/team join <name> - Join a team");
            return true;
        }
        sender.sendMessage(String.format("Joined team %s!", args[1]));
        return true;
    }
    return false;
}
```

To this:
```java
static void registerCommands() {
    CommandTree commandTree=new CommandTree(this);
    commandTree.add(
        "team",
        (commandSender,map)->commandSender.sendMessage("/team - Display this help message"));
    commandTree.add("team create",
        (commandSender,map)->{
            if(map.get("create").length <= 0) commandSender.sendMessage("Usage: /team create <name>");
            else commandSender.sendMessage(String.format("Created team %s!",map.get("create")[0]));
    });
    commandTree.add("team join",
        (commandSender,map)->{
            if(map.get("join").length <= 0) commandSender.sendMessage("Usage: /team join <name>");
            else commandSender.sendMessage(String.format("Joined team %s!",map.get("join")[0]));
    });

    commandTree.register();
}
```
The use of if-else is not needed to parse out subcommands--
only to parse arguments passed to each subcommand.

## Getting started

NOTE: This project is in early development.
There is no stable build; use at your own risk, and check often for updates.

Feel free to report bugs, request features, and open discussions.

### Including CommandTree in your plugin
This plugin currently has no stable release.
To use it, you must clone the project and install to your local Maven repository:
```bash
git clone git@github.com:JellyRekt/CommandRegistrar.git
cd CommandRegistrar
mvn install
```

Then, add this project to your plugin's Maven dependencies:

```xml

<dependency>
	<groupId>com.jellyrekt.commandtreecom.jellyrekt.commandtree</groupId>
	<artifactId>command-registrar</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```

Finally, to use this project's classes in your plugin, you may need to bundle them into your plugin JAR.
One way to do this by adding Maven's shader plugin to your build plugins:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.5.0</version>
    <configuration>
        <filters>
            <filter>
                <artifact>com.jellyrekt.commandtree:command-tree</artifact>
                <includes>
                    <include>com/jellyrekt/commandtree/**</include>
                </includes>
            </filter>
        </filters>
    </configuration>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Using CommandTree

To use the CommandRegistrar, you must do the following in your `onEnable`:
1. Define the CommandExecutor class
2. Instantiate the command tree
3. Add any commands to the tree
4. Register the tree
5. Add base commands to plugin.yml

### Implement CommandExecutor
Rather than using `org.bukkit.CommandExecutor`,
you will implement the class `com.jellyrekt.commandtree.CommandExecutor`.

This interface provides the single method `execute`,
which takes two arguments:
1. The `org.bukkit.CommandSender`
2. A `Map<String, String[]>`

This map provides a reference to the set of args following each command.
For example, we can create the following executor:
```java
public class FooBarCommand implements com.jellyrekt.CommandExecutor {
    @Override
    public void execute(CommandSender sender, Map<String, String[]> env) {
        ...
    }
}
```
If a commandsender executes `/foo Hello world bar goodbye`,
* `env.get("foo")[0]` is set to `"Hello"`
* `env.get("foo")[1]` is set to `"world"`
* `env.get("bar")[0]` is set to `"goobye"`

The parser will automatically detect when the subcommand `bar` has been reached--
so instead of including this as an arg to foo, it will begin a new set of args.

### Add the command to the tree
First, in our `onEnable`, we need to instantiate the `CommandTree`,
passing it an instance of our plugin.
```java
@Override
public void onEnable() {
    CommandTree tree = new CommandTree(this);
    // TODO
}
```

We can now register our `/foo bar` command and add the above implementation of CommandExecutor to our tree.
If we want to add functionality for `/foo`, we can, but we don't need
to register this before registering a subcommand such as `/foo bar`.
We skip any arguments here, just adding a space-seperated list of our subcommand tokens:
```java
public void onEnable() {
    CommandTree tree = new CommandTree(this);
    tree.add("foo bar", new FooBarCommand());
    // TODO
}
```

Finally, after all commands have been added,
we can call `register`.
This tells Bukkit to listen for our command,
and parse it for the correct executor.
```java
public void onEnable() {
    CommandTree tree = new CommandTree(this);
    // Add commands
    tree.add("foo bar", new FooBarCommand());
    tree.add("foo", new FooCommand());
    tree.add("foo baz", new FooBazCommand());
    tree.add("hello world", new HelloWorldCommand());
    // Register
    tree.register();
}
```
#### Require permissions for commands
If you want to check for a permission for players to use your commands,
you can chain the `setPermission` method onto the `add` method.
```java
tree
    .add("foo bar", new FooBarCommand())
    .setPermission("command.foo.bar");
```
By default, if a player attempts to use a command for which they do not have the permission,
they will receive the message `Insufficient permission`.

If you would like to change this message,
you can do so by chaining the `setPermissionDeniedMessage` method.
```java
tree
    .add("foo bar", new FooBarCommand())
    .setPermission("command.foo.bar")
    .setPermissionDeniedMessage("You are not allowed to do that.");
```

If you prefer to change this default message for all commands on the tree,
you can do so by calling this method on the tree before adding any commands.
```java
tree.setPermissionDeniedMessage("You are not allowed to do that.");
tree
    .add("foo bar", new FooBarCommand())
    .setPermission("command.foo.bar");
```

Permissions only determine whether a user can execute a command.
If you would like finer control, such as what arguments can be passed to a command,
you must implement this logic yourself in the CommandExecutor.

### Register base commands to plugin.yml
Bukkit executes our command by listening for command events.
However, to fire a command event, a command registered in plugin.yml must be executed.
Above, we have registered four commands, but internally,
Bukkit only cares about our "base commands": `/foo` and `/hello`.
These are what we need to register in our plugin.yml:
```yaml
# plugin.yml
name: MyPlugin
version: 1.0
main: com.example.MyPlugin
commands:
  foo:
    description: ""
    usage: ""
  hello:
    description: ""
    usage: ""
```
