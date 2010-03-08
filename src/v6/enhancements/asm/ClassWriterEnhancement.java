/***
 * Copyright (c) 2000-2007 INRIA, France Telecom
 * Copyright (c) 2010 Vít Šesták
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package v6.enhancements.asm;

import org.objectweb.asm.ClassWriter;

/**
 * Provides useful tool(s) for {@link ClassWriter}.
 * @author Vít Šesták AKA v6ak
 *
 */
public final class ClassWriterEnhancement {

	private ClassWriterEnhancement(){}
	
	/**
	 * Creates a new {@link ClassWriter}. The {@link ClassWriter} uses specified {@link ClassLoader}.
	 * @param flags see {@link ClassWriter#ClassWriter(int)}
	 * @param classloader {@link ClassLoader} to use
	 * @return the created {@link ClassWriter}
	 */
	public static ClassWriter createClassWriter(int flags, final ClassLoader classloader){
		if (classloader == null) {
			throw new NullPointerException("classloader must not be null");
		}
		return new ClassWriter(flags){
        	@Override
        	protected String getCommonSuperClass(String type1, String type2) {
        		// copied and modified
        		// original version: http://websvn.ow2.org/filedetails.php?repname=asm&path=%2Ftags%2FASM_3_1%2Fasm%2Fsrc%2Forg%2Fobjectweb%2Fasm%2FClassWriter.java
        		Class<?> c, d;
                try {
                    c = Class.forName(type1.replace('/', '.'), true, classloader);
                    d = Class.forName(type2.replace('/', '.'), true, classloader);
                } catch (Exception e) {
                    throw new RuntimeException(e.toString());
                }
                if (c.isAssignableFrom(d)) {
                    return type1;
                }
                if (d.isAssignableFrom(c)) {
                    return type2;
                }
                if (c.isInterface() || d.isInterface()) {
                    return "java/lang/Object";
                } else {
                    do {
                        c = c.getSuperclass();
                    } while (!c.isAssignableFrom(d));
                    return c.getName().replace('.', '/');
                }
        	}
        };
	}
	
}
