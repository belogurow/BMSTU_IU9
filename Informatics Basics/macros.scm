(use-syntax (ice-9 syncase))
(define-syntax when
  (syntax-rules ()
    ((_ cond? . expr) (if cond? (begin . expr)))))

(define-syntax unless
  (syntax-rules ()
    ((_ cond? . expr) (if (not cond?) (begin . expr)))))

(define-syntax for
  (syntax-rules (as in)
    ((_ x in xs . expr) (for-each (lambda (x) (begin . expr)) xs))
    ((_ xs as x . expr) (for x in xs . expr))))

(define-syntax while
  (syntax-rules ()
    ((_ cond? . expr) (let end () (when cond? (begin . expr) (end)))))) 

(define-syntax repeat
  (syntax-rules (until)
    ((_ (exp . expr) until cond?) (let end () (begin exp . expr) (when (not cond?) (end))))))

(define-syntax cout
  (syntax-rules (endl <<)
    ((_ << endl) (newline))
    ((_ << exp) (display exp))
    ((_ << endl . expr) (begin
                          (newline)
                          (cout . expr)))
    ((_ << exp . expr) (begin
                         (display exp)
                         (cout . expr)))))
