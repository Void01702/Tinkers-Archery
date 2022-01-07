package tonite.tinkersarchery.library.projectileinterfaces;

public interface IKnockbackProjectile {

    void setKnockback(float knockback);

    float getKnockback();

    float calculateKnockback();

}
