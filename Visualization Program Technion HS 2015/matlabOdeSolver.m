%options = odeset('RelTol',1e-5,'AbsTol',1e-5 );
Ode(0,0,C,V,U,A,B,N_max);
[T,Y] = ode23(@Ode,[0 100],Materials.');%,options);
graph(Y.',T);