package de.jonasborn.matrix.server.app

interface App {

    public void start(AppManager manager)

    public void activate(AppManager manager)

    public void deactivate(AppManager manager)

}
