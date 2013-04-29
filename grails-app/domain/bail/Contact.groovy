package bail

class Contact {

    String name

    boolean isMe

    static hasMany = [ emailAddresses : EmailAddress ]

    static constraints = {
    }

}
