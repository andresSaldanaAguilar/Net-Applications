try{
    SocketChannel cl = SocketChannel.open();
    cl.configureBlocking(false);
    String host = "127.0.0.1";
    int pto = 1234;
    InetSocketAddress dst = new InetSocketAddress(host,pto);
    Selector sel = Selector.open();
    cl.connect(dst);
    cl.register(sel,SelectionKey.PO_CONNECT);
    while(true){
        sel.select();
        Iterator<SelectionKey> it = sel.selectedKeys().iterator();
        while(it.hasNext()){
            SelectionKey k = (SelectionKey)it.next();
            it.remove();
            if(k.isConnectable()){
                if(cl.isConnectionPending()){
                    System.out.println("Conexion en proceso...");
                    try{
                        cl.finishConnect();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                System.out.println("Conecion establecida exitosamente");
                cl.register(sel,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                continue;
            }
            if(k.isReadable()){
                SocketChannel ch = (SocketChannel)k.channel();
                ByteBuffer b = new ByteBuffer.allocate(1024);
                ch.read(b);
                //....
                continue;
            }
            else if(k.isWritable()){
                SocketChannel ch = (SocketChannel)k.channel();
                String msj = "Un mensaje";
                ByteBuffer b ByteBuffer.wrap(msj.getBytes());
                ch.write(b);
                continue;
            }
        }
    }
}