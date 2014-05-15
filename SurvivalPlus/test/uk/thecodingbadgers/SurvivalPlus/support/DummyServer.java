/**
 * SurvivalPlus 1.0
 * Copyright (C) 2014 CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.thecodingbadgers.SurvivalPlus.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemFactory;

public class DummyServer implements InvocationHandler {
    private static interface MethodHandler {
        Object handle(DummyServer server, Object[] args);
    }

    private static final HashMap<Method, MethodHandler> methods = new HashMap<Method, MethodHandler>();

    static {
        try {
            methods.put(
                    Server.class.getMethod("getItemFactory"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return CraftItemFactory.instance();
                        }
                    }
            );
            methods.put(
                    Server.class.getMethod("getName"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return DummyServer.class.getName();
                        }
                    }
            );
            methods.put(
                    Server.class.getMethod("getVersion"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return "1.7.2";
                        }
                    }
            );
            methods.put(
                    Server.class.getMethod("getBukkitVersion"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return "Bukkit 1.7.2 R0.3";
                        }
                    }
            );
            methods.put(
                    Server.class.getMethod("getLogger"),
                    new MethodHandler() {
                        final Logger logger = Logger.getLogger(DummyServer.class.getCanonicalName());

                        public Object handle(DummyServer server, Object[] args) {
                            return logger;
                        }
                    }
            );
            Bukkit.setServer(Proxy.getProxyClass(Server.class.getClassLoader(), Server.class).asSubclass(Server.class).getConstructor(InvocationHandler.class).newInstance(new DummyServer()));
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    public static void setup() {
    }

    private DummyServer() {
    }

    ;

    public Object invoke(Object proxy, Method method, Object[] args) {
        MethodHandler handler = methods.get(method);
        if (handler != null) {
            return handler.handle(this, args);
        }
        throw new UnsupportedOperationException(String.valueOf(method));
    }
}
