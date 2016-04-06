(define (my-gcd a b)
  (if (= b 0)
      a
      (gcd b (remainder a b))))

(define (my-lcm a b)
  (/ (* a b) (my-gcd a b)))

(define (prime? n)
  (equal? (- n 2) (remainder (expt (- n 2) n) n))) 
   