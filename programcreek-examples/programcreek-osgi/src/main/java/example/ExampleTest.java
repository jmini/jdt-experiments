package example;

import static org.junit.Assert.assertNotNull;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.Test;

public class ExampleTest {

    @Test
    public void testExample() {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        assertNotNull("workspace", workspace);

        System.out.println("location: " + workspace.getRoot().getLocation());
    }

}
