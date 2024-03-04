# AppletWrapperFramework (Work-in-Progress)

AppletWrapperFramework is a lightweight framework for encapsulating Java applets into standalone applications.

## Features

- Converts Java applets into standalone applications.
- Provides a seamless bridge between Java applet-based code and standard Java applications.
- Supports emulation of applet-specific behaviors such as retrieving document base and applet parameters.
- Facilitates the migration of Java applet-based code to standalone applications.

## Usage

1. Add the AppletWrapperFramework library to your project.
2. Implement the JavaAppletAdapter interface to adapt your applet-based code to the framework.
3. Use the provided methods to retrieve document base, applet parameters, and interact with audio clips and images.
4. Compile and run your application as a standalone Java application.

## Example
Change `Applet` to `Panel` or `JPanel` and implement `JavaAppletAdapter`

```java
import javax.swing.*;

public class MyAppletAdapterImpl extends JPanel implements JavaAppletAdapter {
    // Implement methods of the JavaAppletAdapter interface
    // ...
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Your App");
        jFrame.add(new MyAppletAdapterImpl());
        // ...
    }
}
```

# License
AppletWrapperFramework is licensed under the GNU AGPL v3.0. See the LICENSE file for details.