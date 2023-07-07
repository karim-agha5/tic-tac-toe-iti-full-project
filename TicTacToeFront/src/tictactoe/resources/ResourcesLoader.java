/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.resources;

import java.io.InputStream;
import java.net.URL;

public interface ResourcesLoader {
    default InputStream getResourceAsStream(String name) {
        return ResourcesLoader.class.getResourceAsStream(name);
    }
    
    default URL getResource(String name) {
        return ResourcesLoader.class.getResource(name);
    }
    
    default InputStream getImageAsStream(String name) {
        return ResourcesLoader.class.getResourceAsStream("images/" + name);
    }
    
    default InputStream getVideoAsStream(String name) {
        return ResourcesLoader.class.getResourceAsStream("videos/" + name);
    }
    
    default InputStream getFontAsStream(String name) {
        return ResourcesLoader.class.getResourceAsStream("fonts/" + name);
    }
    
    default InputStream getCssAsStream(String name) {
        return ResourcesLoader.class.getResourceAsStream("styles/" + name);
    }

    default URL getImage(String name) {
        return ResourcesLoader.class.getResource("images/" + name);
    }
    
    default URL getVideo(String name) {
        return ResourcesLoader.class.getResource("videos/" + name);
    }
    
    default URL getFont(String name) {
        return ResourcesLoader.class.getResource("fonts/" + name);
    }
    
    default URL getCss(String name) {
        return ResourcesLoader.class.getResource("styles/" + name);
    }
}
