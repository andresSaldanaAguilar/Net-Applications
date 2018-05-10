try{
    ServerSocketChannel s = new ServerSocketChannel.open();
    s.configureBlocking(false);
    s.setOption(StandardSocketOptions.SO_REUSEADDR,true);
    int pto =1234;
    InetSocketAddress l = new InetSocketAddress(pto);
    s.socket().bind();
    //s.bind(l);
    Selector sel = Selector.open();
    s.register(sel,Selectionkey.OP_ACCEPT);
    for(;;){
        sel.select();
        Iterator<Selectionkey> it = sel.selectedKeys().iterator();
        while(it.hasNext()){
            SelectedKey k = (selectionKey)it.next();
            it.remove();
            if(k.isAcceptable){
                SocketChannel cl = s.accept;
                System.out.println("Cliente conectado desde:"+cl.socket.getInetddress()+":"+cl.socket().getPort());
                cl.configureBlocking(false);
                cl.register(sel,SelectionKey.OP_WRITE | Selectionkey.OP_READ);
                continue;
            }
            if(k.isReadable()){
                SocketChannel ch = (SocketChannel)k.channel();
                ByteBuffer b = new ByteBuffer.allocate(1024);
                b.clear();
                int n = ch.read(b);
                if(b.hasArray()){
                    String x = new String(b.array(),0,n);
                    //k.cancel //k.interestOps(SelectionKey.OP_WRITE);
                    continue;
                }
                else if(k.isWritable()){
                    SocketChannel ch = (SocketChannel)k.channel();
                    String msj = "Un mensaje";
                    ByteBuffer b = new ByteBuffer.wrap(msj.getBytes());
                    ch.write(b);
                    continue;
                }
            }
        }
    }
}