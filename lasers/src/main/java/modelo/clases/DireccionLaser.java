package modelo.clases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum DireccionLaser {
    NaE,
    EaS,
    SaO,
    OaN,
    NaO,
    OaS,
    SaE,
    EaN,
    H,
    V;


    public List<Laser> calcularLaserAtravezado(){
        Laser laser;
        Laser laserRecto;
        switch(this){
            case NaE:
                laser = new Laser(DireccionLaser.NaE, new Coordenada(1, 0));
                laserRecto = new Laser(DireccionLaser.V, new Coordenada(0,0));
                break;
            case EaS:
                laser = new Laser(DireccionLaser.EaS, new Coordenada(0, -1));;
                laserRecto = new Laser(DireccionLaser.H, new Coordenada(0,0));
                break;
            case SaO:
                laser =  new Laser(DireccionLaser.SaO, new Coordenada(-1, 0));
                laserRecto = new Laser(DireccionLaser.V, new Coordenada(0,0));
                break;
            case OaN:
                laser = new Laser(DireccionLaser.OaN, new Coordenada(0 , 1));
                laserRecto = new Laser(DireccionLaser.H, new Coordenada(0,0));
                break;
            case NaO:
                laser = new Laser(DireccionLaser.NaO, new Coordenada(1, 0));
                laserRecto = new Laser(DireccionLaser.V, new Coordenada(0,0));
                break;
            case OaS:
                laser = new Laser(DireccionLaser.OaS, new Coordenada(0 , 1));
                laserRecto = new Laser(DireccionLaser.H, new Coordenada(0,0));
                break;
            case SaE:
                laser = new Laser(DireccionLaser.SaE, new Coordenada(-1, 0));
                laserRecto = new Laser(DireccionLaser.V, new Coordenada(0,0));
                break;
            case EaN:
                laser = new Laser(DireccionLaser.EaN, new Coordenada(0 , -1));
                laserRecto = new Laser(DireccionLaser.H, new Coordenada(0,0));
                break;
            case H:
            case V:
                return new ArrayList<Laser>();
            default: throw new IllegalArgumentException("Estado no reconocido: " + this);

        }
        return new ArrayList<Laser>(Arrays.asList(laser,laserRecto));
    }

    public Laser calcularLaser(){
        switch(this){
            case NaE: return new Laser(DireccionLaser.OaS, new Coordenada(0, 1));
            case EaS: return new Laser(DireccionLaser.NaO, new Coordenada(1, 0));
            case SaO: return new Laser(DireccionLaser.EaN, new Coordenada(0, -1));
            case OaN: return new Laser(DireccionLaser.SaE, new Coordenada(-1 , 0));
            case NaO: return new Laser(DireccionLaser.EaS, new Coordenada(0, -1));
            case OaS: return new Laser(DireccionLaser.NaE, new Coordenada(1 , 0));
            case SaE: return new Laser(DireccionLaser.OaN, new Coordenada(0, 1));
            case EaN: return new Laser(DireccionLaser.SaO, new Coordenada(-1 , 0));
            case H:
            case V:
                return null;
            default: throw new IllegalArgumentException("Estado no reconocido: " + this);
        }
    }


    public Laser calcularLazerEspejado(){
        switch(this){
            case NaE: return new Laser(DireccionLaser.SaE, new Coordenada(-1,0) );
            case EaS: return new Laser(DireccionLaser.OaS, new Coordenada(0,1) );
            case SaO: return new Laser(DireccionLaser.NaO, new Coordenada(1,0) );
            case OaN: return new Laser(DireccionLaser.EaN, new Coordenada(0,-1) );
            case NaO: return new Laser(DireccionLaser.SaO, new Coordenada(-1,0) );
            case OaS: return new Laser(DireccionLaser.EaS, new Coordenada(0,-1) );
            case SaE: return new Laser(DireccionLaser.NaE, new Coordenada(1,0) );
            case EaN: return new Laser(DireccionLaser.OaN, new Coordenada(0,1) );
            case H:
            case V:
                return null;
            default: throw new IllegalArgumentException("Estado no reconocido: " + this);
        }
    }
}