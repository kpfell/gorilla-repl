# Gorilla REPL server API

## Managing REPL servers

`/servers/list`

`/servers/add`

`/servers/start`

`/servers/:serverid/stop`

`/servers/:serverid/restart`

`/servers/:serverid/info`


## Managing REPL connections

`/servers/:serverid/connect`

`/connections/list`

`/connections/:connid/disconnect`


## Using REPL connections

`/connections/:connid/evaluate`

`/connection/:connid/completions`


## Worksheet host support

`/config`
