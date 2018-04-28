class Collection{
    private var values: MutableList<String> = mutableListOf()
    private var keys: MutableList<String> = mutableListOf()
    private var hashKeys: MutableList<Long> = mutableListOf()
    private var hashValues: MutableList<Long> = mutableListOf()
    init {
        println("Successfully created")
        println("Write \"Help\" to know the commands")
    }
    //creates hash from string
    private fun makeHash(s:String): Long{
        val p = 31
        var hash:Long = 0
        var pPow:Long = 1
        for (i in 0 until s.length) {
            hash += (s[i] - 'a' + 1) * pPow
            pPow *= p
        }
        return hash % 1000000007
    }
    //adds new pair or changes the value by key and returns true,
    //or returns false if strings are empty
    fun addPair(key:String , value: String): Boolean{
        if (key.isEmpty() || value.isEmpty()) return false
        val hash = makeHash(key)

        //if key exists then change the value
        if (hash in hashKeys){
            val index = hashKeys.indexOf(hash)
            keys[index] = key
            values[index] = value
            hashValues[index] = makeHash(values[index])
        }

        //if key doesn't exist then create new pair
        else{
            hashKeys.add(hash)
            values.add(value)
            keys.add(key)
            hashValues.add(makeHash(value))
        }
        return true
    }
    //returns the value by key, or null if key doesn't exist
    fun getPair(key:String): String?{
        val hash = makeHash(key)
        val index = hashKeys.indexOf(hash)
        if (index == -1)
            return null
        return values[index]
    }
    //deletes pair by key and returns true, or false if the key doesn't exist
    fun deletePairByKey(key:String): Boolean{
        val hash = makeHash(key)
        val index = hashKeys.indexOf(hash)
        if (index == -1) return false
        hashValues.removeAt(index)
        values.removeAt(index)
        keys.removeAt(index)
        hashKeys.remove(hash)
        return true
    }
    //deletes pair by key and returns true, or false if the value doesn't exist
    fun deletePairByValue(value:String): Boolean{
        val hash = makeHash(value)
        var index = hashValues.indexOf(hash)
        if (index == -1) return false
        while(index != -1) {
            hashKeys.removeAt(index)
            values.removeAt(index)
            keys.removeAt(index)
            hashValues.removeAt(index)
            index = hashValues.indexOf(hash)
        }
        return true
    }
    //returns Mutable list of pairs <key and value>
    fun findByKey(key:String): MutableList<Pair<String , String>>{
        val ans: MutableList<Pair<String , String>> = mutableListOf()
        for (i in 0 until keys.size) {
            if (keys[i].indexOf(key) != -1){
                ans.add(Pair(keys[i] , values[i]))
            }
        }
        return ans
    }
    //returns Mutable list of pairs <key and value>
    fun findByValue(key:String): MutableList<Pair<String , String>>{
        val ans: MutableList<Pair<String , String>> = mutableListOf()
        for (i in 0 until values.size){
            if (values[i].indexOf(key) != -1){
                ans.add(Pair(keys[i] , values[i]))
            }
        }
        return ans
    }
    //returns Mutable list of pairs <key and value>
    fun getAll():MutableList<Pair<String , String>>{
        val ans: MutableList<Pair<String , String>> = mutableListOf()
        for (i in 0 until keys.size)
            ans.add(Pair(keys[i] , values[i]))
        return ans
    }
    //returns Mutable list of keys
    fun getKeys() : MutableList<String>{
        return keys
    }
    //erase all pairs
    fun erasePairs(): Boolean{
        values = mutableListOf()
        keys = mutableListOf()
        hashKeys = mutableListOf()
        hashValues = mutableListOf()
        return true
    }
}

fun printDelimiter(){
    println("-------------------------------------------------------------")
}

fun main(args: Array<String>) {
    val commands: MutableList<String> = mutableListOf(
            "HELP" , "SET" , "GET", "FINDK",
            "FINDV" , "DELETEK" , "DELETEV",
            "GETALL", "KEYS", "CLEAR" , "EXIT"
    )
    val descriptions: MutableList<String> = mutableListOf(
            "Prints available commands",
            "Changes the value by key or sets the new pair",
            "Prints the value by key",
            "Prints all of the pairs, which have similar key",
            "Prints all of the pairs, which have similar value",
            "Deletes pair by key",
            "Deletes pair or pairs, if they have the same values",
            "Prints all pairs",
            "Prints all keys",
            "Deletes all pairs",
            "Exits program"
    )
    val safe = Collection()
    printDelimiter()
    while (true){
        print("Write a command: ")
        val cmd = readLine()!!.toUpperCase()
        //if cmd is HELP
        if (cmd == commands[0]){
            for (i in 0 until commands.size){
                println(commands[i] + " - " + descriptions[i])
            }
        }
        //if cmd is SET
        else if (cmd == commands[1]){
            print("Key: ")
            val key = readLine()!!.split(' ')
            print("Value: ")
            val value = readLine()!!.split(' ')
            if (safe.addPair(key[0] , value[0]))
                println("Pair has been added or changed")
            else
                println("Can't add empty strings pair")
        }
        //if cmd is GET
        else if (cmd == commands[2]){
            print("Key: ")
            val lol = readLine()!!.split(' ')
            val v = safe.getPair(lol[0])
            if (v != null)
                println("Value: " + safe.getPair(lol[0]))
            else
                println("Can't find the key")
        }
        //if cmd is FINDK
        else if (cmd == commands[3]){
            print("Search by key: ")
            val lol = readLine()!!.split(' ')
            val ans: MutableList<Pair<String , String>> = safe.findByKey(lol[0])
            for (i in ans){
                println("Pair: " + i.first + " - " + i.second)
            }
            if (ans.size == 0)
                println("No such elements")
        }
        //if cmd is FINDV
        else if (cmd  == commands[4]) {
            print("Search by value: ")
            val lol = readLine()!!.split(' ')
            val ans: MutableList<Pair<String , String>> = safe.findByValue(lol[0])
            for (i in ans){
                println("Pair: " + i.first + " - " + i.second)
            }
            if (ans.size == 0)
                println("No such elements")
        }
        //if cmd is DELETEK
        else if (cmd == commands[5]){
            print("Key: ")
            val lol = readLine()!!.split(' ')
            if (safe.deletePairByKey(lol[0]))
                println("Pair has been deleted")
            else
                println("Can't find a key")
        }
        //if cmd is DELETEV
        else if (cmd == commands[6]){
            print("Value: ")
            val lol = readLine()!!.split(' ')
            if (safe.deletePairByValue(lol[0]))
                println("Pair has been deleted")
            else
                println("Can't find a value")
        }
        //if cmd is GETALL
        else if(cmd == commands[7]){
            for (i in safe.getAll())
                println("Pair: " + i.first + " - " + i.second)
            //if collection is empty
            if (safe.getAll().size == 0)
                println("Collection is empty")
        }
        //if cmd is KEYS
        else if (cmd == commands[8]) {
            var currentKey = 1
            for (i in safe.getKeys()) {
                println("Key $currentKey: $i")
                ++currentKey
            }
            //if collection is empty
            if (currentKey == 1)
                println("Collection is empty")
        }
        //if cmd is CLEAR
        else if (cmd == commands[9]) {
            if (safe.erasePairs())
                println("Pairs has been removed")
        }
        //if cmd is EXIT
        else if (cmd == commands[10]){
            break
        }
        //if cmd isn't in commands
        else
            println("Command \"$cmd\" is not defined")

        printDelimiter()
    }
}




