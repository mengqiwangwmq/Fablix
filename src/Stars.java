class Star {
    public String id;
    public String name;
    public   Integer birthYear;
    public Star(){

    }
    public Star(String name, Integer birthYear){
        //this.id = id;
        this.name = name;
        this.birthYear = birthYear;
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        //sb.append("Employee Details - ");
        sb.append("Name:" + name);
        sb.append(", ");
        //sb.append("Type:" + getType());
        //sb.append(", ");
        sb.append("Id:" + id);
        sb.append(", ");
        sb.append("birthYear:" + birthYear);
        sb.append(".");

        return sb.toString();
    }

}
