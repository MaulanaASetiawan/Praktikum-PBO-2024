abstract class Chara {
    protected String name;
    protected int health, damage, xp;

    public Chara(String name, int health, int damage, int xp) {
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.xp = xp;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }
    
    public int getXp() {
        return xp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setXp(String xp){
        try{
            this.xp = Integer.parseInt(xp);
        }catch (NumberFormatException e){
            System.out.println("Invalid input! Please enter a valid number.");
        }
    }

    abstract void display();
}