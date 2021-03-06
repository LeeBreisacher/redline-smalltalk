/* Redline Smalltalk, Copyright (c) James C. Ladd. All rights reserved. See LICENSE in the root of this distribution */
package st.redline.stout;

import st.redline.ProtoBlock;
import st.redline.ProtoObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouterRegistryImpl implements RouterRegistry {

    private RouterFactory routerFactory;
    private Map<String, List<Router>> routers = new HashMap<String, List<Router>>();

    public RouterRegistryImpl(RouterFactory routerFactory) {
        this.routerFactory = routerFactory;
    }

    public Router register(ProtoObject spec, String type, String method, ProtoObject block) {
//        System.out.println("register() " + spec + " " + type + " " + method + " " + block);
        return register((String) spec.javaValue(), type, method, (ProtoBlock) block);
    }

    public Router register(String spec, String type, String method, ProtoBlock block) {
        Router router = routerFactory.create(spec, type, block);
        getRoutersForMethod(method).add(router);
        return router;
    }

    private List<Router> getRoutersForMethod(String method) {
        if (!routers.containsKey(method)) {
            routers.put(method, new ArrayList<Router>());
        }
        return routers.get(method);
    }

    public Router lookup(ProtoObject path, ProtoObject method) {
        return lookup((String) path.javaValue(), (String) method.javaValue());
    }

    public Router lookup(String path, String method) {
//        System.out.println("lookup() " + path + " " + method);
        for (Router router : getRoutersForMethod(method)) {
            if (router.canHandleRequest(path)) return router;
        }
        return NoOpRouter.INSTANCE;
    }
}
