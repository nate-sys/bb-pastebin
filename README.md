# bb-pastebin
A silly little pastebin clone written in Babashka.

## Usage

### Dev
Run `bb init-db` to initialize a database, then run `bb serve`.

### User
```sh
# creating a paste
curl -X POST -d "this is a paste" http://localhost:8080 # => 200 OK "paste id: 3"

# accessing a paste (3 is the id from above)
curl http://locahost:8080/3 # =>  200 OK "this is a paste"
```
