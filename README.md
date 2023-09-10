# bb-pastebin
A silly little pastebin clone written in Babashka.

## Usage

### Requirements

- Babashka: [Installation](https://github.com/babashka/babashka#installation)
- Sqlite: [Download](https://www.sqlite.org/download.html)

### Dev

```sh
bb init-db         # initialize database
bb serve           # start the server
bb get ID          # get paste with id of ID
bb create CONTENT  # paste the specified CONTENT
```

#### Configuration

Edit the `config.edn` at the root of the repository.

```edn
{:port 8080
 :db-file "./bb-pastebin.db"}
```

### User

```sh
# creating a paste
curl -X POST -d "this is a paste" http://localhost:8080 # => 200 OK "paste id: 3"

# accessing a paste (3 is the id from above)
curl http://locahost:8080/3 # =>  200 OK "this is a paste"
```
