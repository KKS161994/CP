class UserBuilder{
    private String name;
    private int age;
    private String email;

    public UserBuilder(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
    
    class Builder{
        private String name;
        private int age;
        private String email;

        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public Builder setAge(int age){
            this.age = age;
            return this;
        }

        public Builder setEmail(String email){
            this.email = email;
            return this;
        }

        public UserBuilder build(){
            return new UserBuilder(name, age, email);
        }
    }
}