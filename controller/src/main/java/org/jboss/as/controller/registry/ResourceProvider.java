/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.controller.registry;

import java.util.Set;

/**
 * @author Emanuel Muckenhuber
 */
public interface ResourceProvider extends Cloneable {

    boolean has(String name);
    Resource get(String name);
    boolean hasChildren();
    Set<String> children();
    /** Gets the total {@link Resource#getTreeSize() tree size} of all the resources provided by this provider */
    default int getTreeSize() {
        int result = 0;
        for (String name : children()) {
            Resource res = get(name);
            if (res != null) {
                result += res.getTreeSize();
            }
        }
        return result;
    }
    void register(String name, Resource resource);
    void register(String value, int index, Resource resource);
    Resource remove(String name);
    ResourceProvider clone();

    abstract class ResourceProviderRegistry {

        protected abstract void registerResourceProvider(final String type, final ResourceProvider provider);

    }

    class Tool {

        public static void addResourceProvider(final String name, final ResourceProvider provider, final Resource resource) {
            if (resource instanceof ResourceProviderRegistry) {
                ((ResourceProviderRegistry)resource).registerResourceProvider(name, provider);
            } else {
                throw new UnsupportedOperationException();
            }
        }

    }
}
