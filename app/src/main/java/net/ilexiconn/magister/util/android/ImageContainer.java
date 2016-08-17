/*
 * Copyright 2016 Bas van den Boom 'Z3r0byte'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ilexiconn.magister.util.android;

import net.ilexiconn.magister.util.AndroidUtil;
import net.ilexiconn.magister.util.LogUtil;

import java.io.InputStream;
import java.lang.reflect.Method;

public class ImageContainer {
    private Class classT;
    private Class classImage;
    private Class classBitmap;
    private Object image;

    public ImageContainer(InputStream stream) throws ClassNotFoundException {
        Class c;
        if (AndroidUtil.getRunningOnAndroid()) {
            c = Class.forName("android.graphics.Bitmap");
        } else {
            c = Class.forName("java.awt.image.BufferedImage");
        }
        if (c == null) {
            throw new NullPointerException();
        }
        this.classT = c;
        try {
            classBitmap = Class.forName("android.graphics.Bitmap");
            if (!classT.equals(classBitmap)) {
                throw new Exception();
            }
            Method m = Class.forName("android.graphics.BitmapFactory").getMethod("decodeStream", InputStream.class);
            image = classT.cast(m.invoke(null, stream));
            return;
        } catch (Exception e) {
            if (!(e instanceof ClassNotFoundException)) {
                LogUtil.printError(e.getMessage(), e.getCause());
            }
        }
        try {
            classImage = Class.forName("java.awt.image.BufferedImage");
            if (!classT.equals(classImage)) {
                throw new Exception();
            }
            Method m = Class.forName("javax.imageio.ImageIO").getMethod("read", InputStream.class);
            image = classT.cast(m.invoke(null, stream));
            return;
        } catch (Exception e) {
            if (!(e instanceof ClassNotFoundException)) {
                LogUtil.printError(e.getMessage(), e.getCause());
            }
        }
        throw new ClassNotFoundException("Neither the BufferedImage class nor the Bitmap class were found!");
    }

    /**
     * Get the image object. You can check its type bu using {@link ImageContainer#isBitmap()} and
     * {@link ImageContainer#isBufferedImage()}.
     *
     * @return the image object.
     */
    public Object getImage() {
        return image;
    }

    /**
     * Get the class of the image object.
     *
     * @return the class of the image object.
     */
    public Class getImageClass() {
        return classT;
    }

    public boolean isBitmap() {
        return getImageClass().equals(classBitmap);
    }

    public boolean isBufferedImage() {
        return getImageClass().equals(classImage);
    }
}
